package io.devridge.api.service;

import io.devridge.api.config.auth.LoginUser;
import io.devridge.api.domain.companyinfo.*;
import io.devridge.api.domain.roadmap.*;
import io.devridge.api.domain.user.*;
import io.devridge.api.dto.CourseDetailResponseDto;
import io.devridge.api.dto.course.CourseDetailWithAbilityDto;
import io.devridge.api.dto.course.CourseListResponseDto;
import io.devridge.api.dto.course.RoadmapStatusDto;
import io.devridge.api.handler.ex.CompanyInfoNotFoundException;
import io.devridge.api.handler.ex.RoadmapNotMatchCourseAndCompanyInfoException;
import io.devridge.api.handler.ex.UnauthorizedCourseAccessException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

    @InjectMocks
    private CourseService courseService;

    @Mock
    private CompanyInfoRepository companyInfoRepository;

    @Mock
    private CourseDetailRepository courseDetailRepository;

    @Mock
    private RoadmapRepository roadmapRepository;

    @Mock
    private UserRoadmapRepository userRoadmapRepository;


    @DisplayName("코스 목록을 성공적으로 가져온다.")
    @Test
    public void get_course_list_success() {
        // given
        CompanyInfo companyInfo = makeCompanyInfo();
        LoginUser loginUser = LoginUser.builder().user(User.builder().id(1L).build()).build();
        List<RoadmapStatusDto> roadmapStatusDtoList = new ArrayList<>();
        Course course1 = Course.builder().id(1L).name("CS1").type(CourseType.CS).order(1).job(companyInfo.getJob()).build();
        roadmapStatusDtoList.add(RoadmapStatusDto.builder().matchingFlag(MatchingFlag.YES).course(course1).studyStatus(StudyStatus.STUDY_END).build());

        // stub
        when(companyInfoRepository.findByCompanyIdAndJobIdAndDetailedPositionIdWithFetchJoin(anyLong(), anyLong(), anyLong())).thenReturn(Optional.of(companyInfo));
        when(roadmapRepository.getRoadmapsIncludingCoursesByCompanyInfoIdWithUserId(anyLong(), anyLong())).thenReturn(roadmapStatusDtoList);

        // when
        CourseListResponseDto courseListResponseDto = courseService.getCourseList(1L, 1L, 1L, loginUser);

        // then
        assertThat(courseListResponseDto.getCompanyName()).isEqualTo("test company");
        assertThat(courseListResponseDto.getJobName()).isEqualTo("test job");
        assertThat(courseListResponseDto.getCourseList().size()).isEqualTo(1);
        assertThat(courseListResponseDto.getCourseList().get(0).getIndex()).isEqualTo(0);
        assertThat(courseListResponseDto.getCourseList().get(0).getCourses().get(0).getId()).isEqualTo(1L);
        assertThat(courseListResponseDto.getCourseList().get(0).getCourses().get(0).getName()).isEqualTo("CS1");
        assertThat(courseListResponseDto.getCourseList().get(0).getCourses().get(0).getType()).isEqualTo(CourseType.CS);
        assertThat(courseListResponseDto.getCourseList().get(0).getCourses().get(0).getOrder()).isEqualTo(1);
        assertThat(courseListResponseDto.getCourseList().get(0).getCourses().get(0).getMatchingFlag()).isEqualTo(MatchingFlag.YES);
        assertThat(courseListResponseDto.getCourseList().get(0).getCourses().get(0).getStudyStatus()).isEqualTo(StudyStatus.STUDY_END);
    }

    @DisplayName("코스 목록의 첫번째가 SKILL이면 맨 앞에 빈 목록을 추가한다.")
    @Test
    public void get_course_list_if_first_is_skill_add_empty_list() {
        // given
        CompanyInfo companyInfo = makeCompanyInfo();
        List<RoadmapStatusDto> roadmapStatusDtoList = new ArrayList<>();

        Course course1 = Course.builder().id(1L).name("SKILL1").type(CourseType.SKILL).order(2).job(companyInfo.getJob()).build();
        roadmapStatusDtoList.add(RoadmapStatusDto.builder().matchingFlag(MatchingFlag.NO).course(course1).studyStatus(null).build());

        // stub
        when(companyInfoRepository.findByCompanyIdAndJobIdAndDetailedPositionIdWithFetchJoin(anyLong(), anyLong(), anyLong())).thenReturn(Optional.of(companyInfo));
        when(roadmapRepository.getRoadmapsIncludingCoursesByCompanyInfoIdWithUserId(anyLong(), isNull())).thenReturn(roadmapStatusDtoList);

        // when
        CourseListResponseDto courseListResponseDto = courseService.getCourseList(1L, 1L, 1L, null);

        // then
        assertThat(courseListResponseDto.getCourseList().size()).isEqualTo(2);
        assertThat(courseListResponseDto.getCourseList().get(0).getIndex()).isEqualTo(0);
        assertThat(courseListResponseDto.getCourseList().get(0).getCourses()).isEmpty();
        assertThat(courseListResponseDto.getCourseList().get(1).getIndex()).isEqualTo(1);
        assertThat(courseListResponseDto.getCourseList().get(1).getCourses().size()).isEqualTo(1);
        assertThat(courseListResponseDto.getCourseList().get(1).getCourses().get(0).getId()).isEqualTo(1L);
        assertThat(courseListResponseDto.getCourseList().get(1).getCourses().get(0).getName()).isEqualTo("SKILL1");
        assertThat(courseListResponseDto.getCourseList().get(1).getCourses().get(0).getType()).isEqualTo(CourseType.SKILL);
        assertThat(courseListResponseDto.getCourseList().get(1).getCourses().get(0).getMatchingFlag()).isEqualTo(MatchingFlag.NO);
        assertThat(courseListResponseDto.getCourseList().get(1).getCourses().get(0).getOrder()).isEqualTo(2);
    }
    
    @DisplayName("코스 목록의 첫번째가 CS면 맨 앞에 빈 목록을 추가하지 않는다.")
    @Test
    public void get_course_list_if_first_is_cs_not_add_empty_list() {
        // given
        CompanyInfo companyInfo = makeCompanyInfo();
        List<RoadmapStatusDto> roadmapStatusDtoList = new ArrayList<>();
        Course course1 = Course.builder().id(1L).name("CS1").type(CourseType.CS).order(1).job(companyInfo.getJob()).build();
        roadmapStatusDtoList.add(RoadmapStatusDto.builder().matchingFlag(MatchingFlag.YES).course(course1).studyStatus(null).build());

        // stub
        when(companyInfoRepository.findByCompanyIdAndJobIdAndDetailedPositionIdWithFetchJoin(anyLong(), anyLong(), anyLong())).thenReturn(Optional.of(companyInfo));
        when(roadmapRepository.getRoadmapsIncludingCoursesByCompanyInfoIdWithUserId(anyLong(), isNull())).thenReturn(roadmapStatusDtoList);

        // when
        CourseListResponseDto courseListResponseDto = courseService.getCourseList(1L, 1L, 1L, null);

        // then
        assertThat(courseListResponseDto.getCourseList().size()).isEqualTo(1);
        assertThat(courseListResponseDto.getCourseList().get(0).getIndex()).isEqualTo(0);
        assertThat(courseListResponseDto.getCourseList().get(0).getCourses().size()).isEqualTo(1);
        assertThat(courseListResponseDto.getCourseList().get(0).getCourses().get(0).getId()).isEqualTo(1L);
        assertThat(courseListResponseDto.getCourseList().get(0).getCourses().get(0).getName()).isEqualTo("CS1");
        assertThat(courseListResponseDto.getCourseList().get(0).getCourses().get(0).getType()).isEqualTo(CourseType.CS);
        assertThat(courseListResponseDto.getCourseList().get(0).getCourses().get(0).getMatchingFlag()).isEqualTo(MatchingFlag.YES);
        assertThat(courseListResponseDto.getCourseList().get(0).getCourses().get(0).getOrder()).isEqualTo(1);
    }

    @DisplayName("SKILL 다음에 SKILL이 나오면 중간에 빈 목록을 추가한다.")
    @Test
    public void get_course_list_if_skill_between_skill_add_empty_list() {
        // given
        CompanyInfo companyInfo = makeCompanyInfo();
        List<RoadmapStatusDto> roadmapStatusDtoList = new ArrayList<>();
        Course course1 = Course.builder().id(1L).name("CS1").type(CourseType.CS).order(1).job(companyInfo.getJob()).build();
        Course course2 = Course.builder().id(2L).name("SKILL1").type(CourseType.SKILL).order(2).job(companyInfo.getJob()).build();
        Course course3 = Course.builder().id(3L).name("SKILL2").type(CourseType.SKILL).order(4).job(companyInfo.getJob()).build();
        roadmapStatusDtoList.add(RoadmapStatusDto.builder().matchingFlag(MatchingFlag.NO).course(course1).build());
        roadmapStatusDtoList.add(RoadmapStatusDto.builder().matchingFlag(MatchingFlag.YES).course(course2).build());
        roadmapStatusDtoList.add(RoadmapStatusDto.builder().matchingFlag(MatchingFlag.YES).course(course3).build());

        // stub
        when(companyInfoRepository.findByCompanyIdAndJobIdAndDetailedPositionIdWithFetchJoin(anyLong(), anyLong(), anyLong())).thenReturn(Optional.of(companyInfo));
        when(roadmapRepository.getRoadmapsIncludingCoursesByCompanyInfoIdWithUserId(anyLong(), isNull())).thenReturn(roadmapStatusDtoList);

        // when
        CourseListResponseDto courseListResponseDto = courseService.getCourseList(1L, 1L, 1L, null);

        // then
        assertThat(courseListResponseDto.getCourseList().size()).isEqualTo(4);
        assertThat(courseListResponseDto.getCourseList().get(0).getCourses().get(0).getName()).isEqualTo("CS1");
        assertThat(courseListResponseDto.getCourseList().get(0).getCourses().get(0).getType()).isEqualTo(CourseType.CS);
        assertThat(courseListResponseDto.getCourseList().get(1).getCourses().get(0).getName()).isEqualTo("SKILL1");
        assertThat(courseListResponseDto.getCourseList().get(1).getCourses().get(0).getType()).isEqualTo(CourseType.SKILL);
        assertThat(courseListResponseDto.getCourseList().get(2).getCourses()).isEmpty();
        assertThat(courseListResponseDto.getCourseList().get(3).getCourses().get(0).getName()).isEqualTo("SKILL2");
        assertThat(courseListResponseDto.getCourseList().get(3).getCourses().get(0).getType()).isEqualTo(CourseType.SKILL);
    }

    @DisplayName("SKILL 다음에 CS가 나오면 빈 목록을 추가 하지 않는다.")
    @Test
    public void get_course_list_if_skill_between_cs_not_add_empty_list() {
        // given
        CompanyInfo companyInfo = makeCompanyInfo();
        List<RoadmapStatusDto> roadmapStatusDtoList = new ArrayList<>();
        Course course1 = Course.builder().id(1L).name("CS1").type(CourseType.CS).order(1).job(companyInfo.getJob()).build();
        Course course2 = Course.builder().id(2L).name("SKILL1").type(CourseType.SKILL).order(2).job(companyInfo.getJob()).build();
        Course course3 = Course.builder().id(3L).name("CS2").type(CourseType.CS).order(4).job(companyInfo.getJob()).build();
        roadmapStatusDtoList.add(RoadmapStatusDto.builder().matchingFlag(MatchingFlag.NO).course(course1).build());
        roadmapStatusDtoList.add(RoadmapStatusDto.builder().matchingFlag(MatchingFlag.YES).course(course2).build());
        roadmapStatusDtoList.add(RoadmapStatusDto.builder().matchingFlag(MatchingFlag.YES).course(course3).build());
        
        // stub
        when(companyInfoRepository.findByCompanyIdAndJobIdAndDetailedPositionIdWithFetchJoin(anyLong(), anyLong(), anyLong())).thenReturn(Optional.of(companyInfo));
        when(roadmapRepository.getRoadmapsIncludingCoursesByCompanyInfoIdWithUserId(anyLong(), isNull())).thenReturn(roadmapStatusDtoList);

        // when
        CourseListResponseDto courseListResponseDto = courseService.getCourseList(1L, 1L, 1L, null);

        // then
        assertThat(courseListResponseDto.getCourseList().size()).isEqualTo(3);
        assertThat(courseListResponseDto.getCourseList().get(0).getCourses().get(0).getName()).isEqualTo("CS1");
        assertThat(courseListResponseDto.getCourseList().get(0).getCourses().get(0).getType()).isEqualTo(CourseType.CS);
        assertThat(courseListResponseDto.getCourseList().get(1).getCourses().get(0).getName()).isEqualTo("SKILL1");
        assertThat(courseListResponseDto.getCourseList().get(1).getCourses().get(0).getType()).isEqualTo(CourseType.SKILL);
        assertThat(courseListResponseDto.getCourseList().get(2).getCourses().get(0).getName()).isEqualTo("CS2");
        assertThat(courseListResponseDto.getCourseList().get(2).getCourses().get(0).getType()).isEqualTo(CourseType.CS);
    }

    @DisplayName("같은 순서를 가진 코스는 같은 목록에 포함된다")
    @Test
    public void same_order_course_is_same_list() {
        // given
        CompanyInfo companyInfo = makeCompanyInfo();
        List<RoadmapStatusDto> roadmapStatusDtoList = new ArrayList<>();
        Course course1 = Course.builder().id(1L).name("CS1").type(CourseType.CS).order(1).job(companyInfo.getJob()).build();
        Course course2 = Course.builder().id(1L).name("CS2").type(CourseType.CS).order(1).job(companyInfo.getJob()).build();
        Course course3 = Course.builder().id(1L).name("SKILL1").type(CourseType.SKILL).order(2).job(companyInfo.getJob()).build();
        roadmapStatusDtoList.add(RoadmapStatusDto.builder().matchingFlag(MatchingFlag.NO).course(course1).build());
        roadmapStatusDtoList.add(RoadmapStatusDto.builder().matchingFlag(MatchingFlag.YES).course(course2).build());
        roadmapStatusDtoList.add(RoadmapStatusDto.builder().matchingFlag(MatchingFlag.YES).course(course3).build());

        // stub
        when(companyInfoRepository.findByCompanyIdAndJobIdAndDetailedPositionIdWithFetchJoin(anyLong(), anyLong(), anyLong())).thenReturn(Optional.of(companyInfo));
        when(roadmapRepository.getRoadmapsIncludingCoursesByCompanyInfoIdWithUserId(anyLong(), isNull())).thenReturn(roadmapStatusDtoList);

        // when
        CourseListResponseDto courseListResponseDto = courseService.getCourseList(1L, 1L, 1L, null);

        // then
        assertThat(courseListResponseDto.getCourseList().size()).isEqualTo(2);
        assertThat(courseListResponseDto.getCourseList().get(0).getCourses().size()).isEqualTo(2);
        assertThat(courseListResponseDto.getCourseList().get(0).getCourses().get(0).getName()).isEqualTo("CS1");
        assertThat(courseListResponseDto.getCourseList().get(0).getCourses().get(0).getType()).isEqualTo(CourseType.CS);
        assertThat(courseListResponseDto.getCourseList().get(0).getCourses().get(1).getName()).isEqualTo("CS2");
        assertThat(courseListResponseDto.getCourseList().get(0).getCourses().get(1).getType()).isEqualTo(CourseType.CS);
        assertThat(courseListResponseDto.getCourseList().get(1).getCourses().get(0).getName()).isEqualTo("SKILL1");
        assertThat(courseListResponseDto.getCourseList().get(1).getCourses().get(0).getType()).isEqualTo(CourseType.SKILL);
    }

    @DisplayName("코스 목록을 가져올 때 맞는 회사 정보를 찾을 수 없으면 예외를 발생시킨다.")
    @Test
    public void get_course_list_if_not_found_company_info_throw_exception() {
        // stub
        when(companyInfoRepository.findByCompanyIdAndJobIdAndDetailedPositionIdWithFetchJoin(anyLong(), anyLong(), anyLong())).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> courseService.getCourseList(1L, 1L, 1L, null))
                .isInstanceOf(CompanyInfoNotFoundException.class);
    }

    /**
     * 코스 상세 테스트
     */

    @DisplayName("코스 상세 목록과 회사 요구 스킬이 일치하는 항목이 없으면 모든 코스 상세 목록을 가져온다.")
    @Test
    void not_match_course_detail_and_company_required_skill_get_all_course_detail_list() {
        //given
        LoginUser loginUser = LoginUser.builder().user(User.builder().id(1L).build()).build();
        CompanyInfo companyInfo = CompanyInfo.builder().id(1L).build();
        Course course = Course.builder().id(1L).name("언어").build();
        Roadmap roadmap = Roadmap.builder().id(1L).course(course).companyInfo(companyInfo).build();
        List<CourseDetailWithAbilityDto> courseDetailList = new ArrayList<>();
        courseDetailList.add(CourseDetailWithAbilityDto.builder().courseDetailId(1L).courseDetailName("C").build());
        courseDetailList.add(CourseDetailWithAbilityDto.builder().courseDetailId(2L).courseDetailName("C++").build());
        courseDetailList.add(CourseDetailWithAbilityDto.builder().courseDetailId(3L).courseDetailName("Java").build());
        courseDetailList.add(CourseDetailWithAbilityDto.builder().courseDetailId(4L).courseDetailName("Kotlin").build());
        
        //stub
        when(companyInfoRepository.findByCompanyIdAndJobIdAndDetailedPositionId(anyLong(), anyLong(), anyLong())).thenReturn(Optional.of(companyInfo));
        when(roadmapRepository.findRoadmapWithCourseByCourseIdAndCompanyInfoId(anyLong(), anyLong())).thenReturn(Optional.of(roadmap));
        when(courseDetailRepository.getMatchingCourseDetailIdsForCompanyAbility(anyLong(), anyLong(), anyLong())).thenReturn(new ArrayList<>());
        when(courseDetailRepository.getCourseDetailListWithAbilityByCourseIdOrderByName(anyLong(), anyList())).thenReturn(courseDetailList);
        when(userRoadmapRepository.findByUserIdAndRoadmapId(anyLong(), anyLong())).thenReturn(Optional.empty());

        //when
        CourseDetailResponseDto courseDetailResponseDto = courseService.getCourseDetailList(1L, 1L, 1L, 1L, loginUser);

        //then
        assertThat(courseDetailResponseDto.getCourseName()).isEqualTo("언어");
        assertThat(courseDetailResponseDto.getCourseDetails().size()).isEqualTo(4);
        assertThat(courseDetailResponseDto.getLoginStatus()).isEqualTo(LoginStatus.YES);
        assertThat(courseDetailResponseDto.getStudyStatus()).isNull();
        assertThat(courseDetailResponseDto.getCourseDetails().get(0).getId()).isEqualTo(1L);
        assertThat(courseDetailResponseDto.getCourseDetails().get(0).getName()).isEqualTo("C");
        assertThat(courseDetailResponseDto.getCourseDetails().get(1).getId()).isEqualTo(2L);
        assertThat(courseDetailResponseDto.getCourseDetails().get(1).getName()).isEqualTo("C++");
        assertThat(courseDetailResponseDto.getCourseDetails().get(2).getId()).isEqualTo(3L);
        assertThat(courseDetailResponseDto.getCourseDetails().get(2).getName()).isEqualTo("Java");
        assertThat(courseDetailResponseDto.getCourseDetails().get(3).getId()).isEqualTo(4L);
        assertThat(courseDetailResponseDto.getCourseDetails().get(3).getName()).isEqualTo("Kotlin");
    }

    @DisplayName("코스 상세 목록과 회사 요구 스킬이 일치하는 항목이 있으면 그 항목만 가져온다.")
    @Test
    void match_course_detail_and_company_required_skill_get_all_course_detail_list() {
        //given
        LoginUser loginUser = LoginUser.builder().user(User.builder().id(1L).build()).build();
        CompanyInfo companyInfo = CompanyInfo.builder().id(1L).build();
        Course course = Course.builder().id(1L).name("언어").build();
        Roadmap roadmap = Roadmap.builder().id(1L).course(course).companyInfo(companyInfo).build();
        List<Long> matchingCourseDetailIds = new ArrayList<>();
        matchingCourseDetailIds.add(1L);
        matchingCourseDetailIds.add(3L);
        List<CourseDetailWithAbilityDto> courseDetailList = new ArrayList<>();
        courseDetailList.add(CourseDetailWithAbilityDto.builder().courseDetailId(1L).courseDetailName("C").build());
        courseDetailList.add(CourseDetailWithAbilityDto.builder().courseDetailId(3L).courseDetailName("Java").build());

        //stub
        when(companyInfoRepository.findByCompanyIdAndJobIdAndDetailedPositionId(anyLong(), anyLong(), anyLong())).thenReturn(Optional.of(companyInfo));
        when(roadmapRepository.findRoadmapWithCourseByCourseIdAndCompanyInfoId(anyLong(), anyLong())).thenReturn(Optional.of(roadmap));
        when(courseDetailRepository.getMatchingCourseDetailIdsForCompanyAbility(anyLong(), anyLong(), anyLong())).thenReturn(matchingCourseDetailIds);
        when(courseDetailRepository.getCourseDetailListWithAbilityByCourseIdOrderByName(anyLong(), anyList())).thenReturn(courseDetailList);
        when(userRoadmapRepository.findByUserIdAndRoadmapId(anyLong(), anyLong())).thenReturn(Optional.empty());

        //when
        CourseDetailResponseDto courseDetailResponseDto = courseService.getCourseDetailList(1L, 1L, 1L, 1L, loginUser);

        //then
        assertThat(courseDetailResponseDto.getCourseName()).isEqualTo("언어");
        assertThat(courseDetailResponseDto.getCourseDetails().size()).isEqualTo(2);
        assertThat(courseDetailResponseDto.getCourseDetails().get(0).getId()).isEqualTo(1L);
        assertThat(courseDetailResponseDto.getCourseDetails().get(0).getName()).isEqualTo("C");
        assertThat(courseDetailResponseDto.getCourseDetails().get(1).getId()).isEqualTo(3L);
        assertThat(courseDetailResponseDto.getCourseDetails().get(1).getName()).isEqualTo("Java");
    }

    @DisplayName("코스 상세 목록과 회사 요구 스킬이 일치하는 항목이 없으면 모든 코스 상세 목록을 가져온다.")
    @Test
    void not_match_course_detail_and_company_required_skill_get_all_course_detail_list11() {
        //given
        LoginUser loginUser = LoginUser.builder().user(User.builder().id(1L).build()).build();
        CompanyInfo companyInfo = CompanyInfo.builder().id(1L).build();
        Course course = Course.builder().id(1L).name("언어").build();
        Roadmap roadmap = Roadmap.builder().id(1L).course(course).companyInfo(companyInfo).build();
        List<CourseDetailWithAbilityDto> courseDetailList = new ArrayList<>();
        courseDetailList.add(CourseDetailWithAbilityDto.builder().courseDetailId(1L).courseDetailName("C").build());
        courseDetailList.add(CourseDetailWithAbilityDto.builder().courseDetailId(2L).courseDetailName("C++").build());
        courseDetailList.add(CourseDetailWithAbilityDto.builder().courseDetailId(3L).courseDetailName("Java").build());
        courseDetailList.add(CourseDetailWithAbilityDto.builder().courseDetailId(4L).courseDetailName("Kotlin").build());

        //stub
        when(companyInfoRepository.findByCompanyIdAndJobIdAndDetailedPositionId(anyLong(), anyLong(), anyLong())).thenReturn(Optional.of(companyInfo));
        when(roadmapRepository.findRoadmapWithCourseByCourseIdAndCompanyInfoId(anyLong(), anyLong())).thenReturn(Optional.of(roadmap));
        when(courseDetailRepository.getMatchingCourseDetailIdsForCompanyAbility(anyLong(), anyLong(), anyLong())).thenReturn(new ArrayList<>());
        when(courseDetailRepository.getCourseDetailListWithAbilityByCourseIdOrderByName(anyLong(), anyList())).thenReturn(courseDetailList);
        when(userRoadmapRepository.findByUserIdAndRoadmapId(anyLong(), anyLong())).thenReturn(Optional.empty());

        //when
        CourseDetailResponseDto courseDetailResponseDto = courseService.getCourseDetailList(1L, 1L, 1L, 1L, loginUser);

        //then
        assertThat(courseDetailResponseDto.getCourseName()).isEqualTo("언어");
        assertThat(courseDetailResponseDto.getCourseDetails().size()).isEqualTo(4);
        assertThat(courseDetailResponseDto.getLoginStatus()).isEqualTo(LoginStatus.YES);
        assertThat(courseDetailResponseDto.getStudyStatus()).isNull();
        assertThat(courseDetailResponseDto.getCourseDetails().get(0).getId()).isEqualTo(1L);
        assertThat(courseDetailResponseDto.getCourseDetails().get(0).getName()).isEqualTo("C");
        assertThat(courseDetailResponseDto.getCourseDetails().get(1).getId()).isEqualTo(2L);
        assertThat(courseDetailResponseDto.getCourseDetails().get(1).getName()).isEqualTo("C++");
        assertThat(courseDetailResponseDto.getCourseDetails().get(2).getId()).isEqualTo(3L);
        assertThat(courseDetailResponseDto.getCourseDetails().get(2).getName()).isEqualTo("Java");
        assertThat(courseDetailResponseDto.getCourseDetails().get(3).getId()).isEqualTo(4L);
        assertThat(courseDetailResponseDto.getCourseDetails().get(3).getName()).isEqualTo("Kotlin");
    }

    @DisplayName("로그인한 유저가 해당 코스 상세에 대한 학습 정보를 가지고 있으면 가져온다.")
    @Test
    void get_study_status_if_login_user_have_study_status_with_course_detail() {
        //given
        LoginUser loginUser = LoginUser.builder().user(User.builder().id(1L).build()).build();
        CompanyInfo companyInfo = CompanyInfo.builder().id(1L).build();
        Course course = Course.builder().id(1L).name("언어").build();
        Roadmap roadmap = Roadmap.builder().id(1L).course(course).companyInfo(companyInfo).build();
        UserRoadmap userRoadmap = UserRoadmap.builder().id(1L).user(loginUser.getUser()).studyStatus(StudyStatus.STUDYING).roadmap(roadmap).build();

        //stub
        when(companyInfoRepository.findByCompanyIdAndJobIdAndDetailedPositionId(anyLong(), anyLong(), anyLong())).thenReturn(Optional.of(companyInfo));
        when(roadmapRepository.findRoadmapWithCourseByCourseIdAndCompanyInfoId(anyLong(), anyLong())).thenReturn(Optional.of(roadmap));
        when(courseDetailRepository.getMatchingCourseDetailIdsForCompanyAbility(anyLong(), anyLong(), anyLong())).thenReturn(new ArrayList<>());
        when(courseDetailRepository.getCourseDetailListWithAbilityByCourseIdOrderByName(anyLong(), anyList())).thenReturn(new ArrayList<>());
        when(userRoadmapRepository.findByUserIdAndRoadmapId(anyLong(), anyLong())).thenReturn(Optional.of(userRoadmap));

        //when
        CourseDetailResponseDto courseDetailResponseDto = courseService.getCourseDetailList(1L, 1L, 1L, 1L, loginUser);

        //then
        assertThat(courseDetailResponseDto.getLoginStatus()).isEqualTo(LoginStatus.YES);
        assertThat(courseDetailResponseDto.getStudyStatus()).isEqualTo(StudyStatus.STUDYING);
    }

    @DisplayName("로그인 안한 유저가 허용된 코스 상세 목록 요청시 성공적으로 가져온다.")
    @Test
    void get_allowed_course_detail_list_with_not_login_user() {
        //given
        CompanyInfo companyInfo = CompanyInfo.builder().id(1L).build();
        List<Roadmap> roadmapList = new ArrayList<>();
        Roadmap allowRoadmap = Roadmap.builder().id(1L).course(Course.builder().id(1L).name("언어").build()).build();
        roadmapList.add(allowRoadmap);
        roadmapList.add(Roadmap.builder().id(2L).course(Course.builder().id(2L).name("프레임 워크").build()).build());

        List<CourseDetailWithAbilityDto> courseDetailList = new ArrayList<>();
        courseDetailList.add(CourseDetailWithAbilityDto.builder().courseDetailId(1L).courseDetailName("Java").build());
        courseDetailList.add(CourseDetailWithAbilityDto.builder().courseDetailId(2L).courseDetailName("Kotlin").build());

        //stub
        when(companyInfoRepository.findByCompanyIdAndJobIdAndDetailedPositionId(anyLong(), anyLong(), anyLong())).thenReturn(Optional.of(companyInfo));
        when(roadmapRepository.findRoadmapWithCourseByCourseIdAndCompanyInfoId(anyLong(), anyLong())).thenReturn(Optional.of(allowRoadmap));
        when(roadmapRepository.findTop2ByCompanyInfoIdOrderByCourseOrder(anyLong())).thenReturn(roadmapList);
        when(courseDetailRepository.getMatchingCourseDetailIdsForCompanyAbility(anyLong(), anyLong(), anyLong())).thenReturn(new ArrayList<>());
        when(courseDetailRepository.getCourseDetailListWithAbilityByCourseIdOrderByName(anyLong(), anyList())).thenReturn(courseDetailList);

        //when
        CourseDetailResponseDto courseDetailResponseDto = courseService.getCourseDetailList(1L, 1L, 1L, 1L, null);

        //then
        assertThat(courseDetailResponseDto.getCourseName()).isEqualTo("언어");
        assertThat(courseDetailResponseDto.getLoginStatus()).isEqualTo(LoginStatus.NO);
        assertThat(courseDetailResponseDto.getStudyStatus()).isNull();
        assertThat(courseDetailResponseDto.getCourseDetails().size()).isEqualTo(2);
        assertThat(courseDetailResponseDto.getCourseDetails().get(0).getId()).isEqualTo(1L);
        assertThat(courseDetailResponseDto.getCourseDetails().get(0).getName()).isEqualTo("Java");
        assertThat(courseDetailResponseDto.getCourseDetails().get(1).getId()).isEqualTo(2L);
        assertThat(courseDetailResponseDto.getCourseDetails().get(1).getName()).isEqualTo("Kotlin");
    }

    @DisplayName("로그인 안한 유저가 허용되지 않은 코스 상세 목록 요청시 예외를 발생시킨다.")
    @Test
    void get_not_allowed_course_detail_list_with_not_login_user_throw_exception() {
        //given
        CompanyInfo companyInfo = CompanyInfo.builder().id(1L).build();
        List<Roadmap> allowRoadmapList = new ArrayList<>();
        allowRoadmapList.add(Roadmap.builder().id(1L).course(Course.builder().id(1L).build()).build());
        allowRoadmapList.add(Roadmap.builder().id(2L).course(Course.builder().id(2L).build()).build());

        Roadmap notAllowRoadmap = Roadmap.builder().id(3L).course(Course.builder().id(3L).build()).build();

        //stub
        when(companyInfoRepository.findByCompanyIdAndJobIdAndDetailedPositionId(anyLong(), anyLong(), anyLong())).thenReturn(Optional.of(companyInfo));
        when(roadmapRepository.findRoadmapWithCourseByCourseIdAndCompanyInfoId(anyLong(), anyLong())).thenReturn(Optional.of(notAllowRoadmap));
        when(roadmapRepository.findTop2ByCompanyInfoIdOrderByCourseOrder(anyLong())).thenReturn(allowRoadmapList);

        //when & then
        assertThatThrownBy(() -> courseService.getCourseDetailList(4L, 1L, 1L, 1L, null))
                .isInstanceOf(UnauthorizedCourseAccessException.class);
    }
    
    @DisplayName("코스 상세 목록을 가져올 때 맞는 회사 정보를 찾을 수 없으면 예외를 발생시킨다.")
    @Test
    void get_course_detail_list_if_not_found_company_info_throw_exception() {
        //given
        LoginUser loginUser = LoginUser.builder().user(User.builder().id(1L).build()).build();

        //stub
        when(companyInfoRepository.findByCompanyIdAndJobIdAndDetailedPositionId(anyLong(), anyLong(), anyLong())).thenReturn(Optional.empty());

        //when & then
        assertThatThrownBy(() -> courseService.getCourseDetailList(1L, 1L, 1L, 1L, loginUser))
                .isInstanceOf(CompanyInfoNotFoundException.class);
    }

    @DisplayName("코스 상세 목록을 가져올 때 회사정보와 코스에 맞는 로드맵 정보를 찾을 수 없으면 예외를 발생시킨다.")
    @Test
    void get_course_detail_list_if_not_found_course_throw_exception() {
        //given
        CompanyInfo companyInfo = CompanyInfo.builder().id(1L).build();
        LoginUser loginUser = LoginUser.builder().user(User.builder().id(1L).build()).build();

        //stub
        when(companyInfoRepository.findByCompanyIdAndJobIdAndDetailedPositionId(anyLong(), anyLong(), anyLong())).thenReturn(Optional.of(companyInfo));
        when(roadmapRepository.findRoadmapWithCourseByCourseIdAndCompanyInfoId(anyLong(), anyLong())).thenReturn(Optional.empty());

        //when & then
        assertThatThrownBy(() -> courseService.getCourseDetailList(1L, 1L, 1L, 1L, loginUser))
                .isInstanceOf(RoadmapNotMatchCourseAndCompanyInfoException.class);
    }

    private CompanyInfo makeCompanyInfo() {
        Company company = Company.builder().id(1L).name("test company").build();
        Job job = Job.builder().id(1L).name("test job").build();
        DetailedPosition detailedPosition = DetailedPosition.builder().id(1L).name("test detailed position").build();
        return CompanyInfo.builder().id(1L).company(company).job(job).detailedPosition(detailedPosition).build();
    }
}