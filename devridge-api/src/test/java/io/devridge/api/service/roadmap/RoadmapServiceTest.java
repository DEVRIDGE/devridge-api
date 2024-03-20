package io.devridge.api.service.roadmap;

import io.devridge.api.config.auth.LoginUser;
import io.devridge.api.dto.education_materials.CourseVideoWithLikeDto;
import io.devridge.api.dto.roadmap.CourseDetailResponseDto;
import io.devridge.api.dto.roadmap.CourseItemResponseDto;
import io.devridge.api.dto.roadmap.RoadmapAndStatusDto;
import io.devridge.api.dto.roadmap.RoadmapDto;
import io.devridge.api.handler.ex.CourseDetailNotFoundException;
import io.devridge.api.handler.ex.RoadmapNotMatchCourseAndCompanyInfoException;
import io.devridge.api.handler.ex.UnAuthorizedCourseAccessException;
import io.devridge.api.handler.ex.recruitment.RecruitmentInfoNotFoundException;
import io.devridge.api.repository.roadmap.ApiCourseDetailRepository;
import io.devridge.api.repository.roadmap.ApiCourseToDetailRepository;
import io.devridge.api.repository.roadmap.ApiRoadmapRepository;
import io.devridge.api.repository.roadmap.ApiUserRoadmapRepository;
import io.devridge.api.service.education_materials.EducationMaterialService;
import io.devridge.api.service.recruitment.RecruitmentInfoService;
import io.devridge.core.domain.company.Company;
import io.devridge.core.domain.company.DetailedPosition;
import io.devridge.core.domain.company.Job;
import io.devridge.core.domain.education_materials.book.BookLanguage;
import io.devridge.core.domain.education_materials.book.BookSource;
import io.devridge.core.domain.education_materials.book.CourseBook;
import io.devridge.core.domain.education_materials.video.VideoSource;
import io.devridge.core.domain.recruitment.RecruitmentInfo;
import io.devridge.core.domain.roadmap.*;
import io.devridge.core.domain.user.LoginStatus;
import io.devridge.core.domain.user.StudyStatus;
import io.devridge.core.domain.user.User;
import io.devridge.core.domain.user.UserRoadmap;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoadmapServiceTest {

    @InjectMocks
    private RoadmapService roadmapService;

    @Mock
    private RecruitmentInfoService recruitmentInfoService;

    @Mock
    private EducationMaterialService educationMaterialService;

    @Mock
    private ApiCourseDetailRepository courseDetailRepository;

    @Mock
    private ApiRoadmapRepository roadmapRepository;

    @Mock
    private ApiUserRoadmapRepository userRoadmapRepository;

    @Mock
    private ApiCourseToDetailRepository courseToDetailRepository;

    /**
     * 로드맵 테스트
     */
    @DisplayName("로드맵을 성공적으로 가져온다.")
    @Test
    void getRoadmap_success_test() {
        // given
        Long jobId = 1L;
        Long detailedPositionId = 1L;
        LoginUser loginUser = LoginUser.builder().user(User.builder().id(1L).build()).build();

        // stub
        RecruitmentInfo recruitmentInfo = makeRecruitmentInfo();
        when(recruitmentInfoService.findRecruitmentInfo(anyLong(), anyLong())).thenReturn(Optional.of(recruitmentInfo));

        Course course1 = Course.builder().id(1L).name("CS1").type(CourseType.CS).order(1).job(recruitmentInfo.getJob()).build();
        Course course2 = Course.builder().id(2L).name("SKILL1").type(CourseType.SKILL).order(2).job(recruitmentInfo.getJob()).build();
        List<RoadmapAndStatusDto> roadmapAndStatusDtoList = new ArrayList<>();
        roadmapAndStatusDtoList.add(RoadmapAndStatusDto.builder().matchingFlag(MatchingFlag.YES).course(course1).studyStatus(StudyStatus.STUDYING).build());
        roadmapAndStatusDtoList.add(RoadmapAndStatusDto.builder().matchingFlag(MatchingFlag.YES).course(course2).studyStatus(null).build());
        when(roadmapRepository.getRoadmapListByRecruitmentInfoIdAndUserId(anyLong(), anyLong())).thenReturn(roadmapAndStatusDtoList);

        // when
        RoadmapDto roadmapDto = roadmapService.getRoadmap(jobId, detailedPositionId, loginUser);

        // then
        assertThat(roadmapDto.getCompanyName()).isEqualTo("test company");
        assertThat(roadmapDto.getJobName()).isEqualTo("test job");
        assertThat(roadmapDto.getRecruitmentInfoUrl()).isEqualTo("companyInfoUrl");
        assertThat(roadmapDto.getCourseList().size()).isEqualTo(2);
        assertThat(roadmapDto.getCourseList().get(0).getIndex()).isEqualTo(0);
        assertThat(roadmapDto.getCourseList().get(0).getCourses().get(0).getId()).isEqualTo(1L);
        assertThat(roadmapDto.getCourseList().get(0).getCourses().get(0).getName()).isEqualTo("CS1");
        assertThat(roadmapDto.getCourseList().get(0).getCourses().get(0).getType()).isEqualTo(CourseType.CS);
        assertThat(roadmapDto.getCourseList().get(0).getCourses().get(0).getMatchingFlag()).isEqualTo(MatchingFlag.YES);
        assertThat(roadmapDto.getCourseList().get(0).getCourses().get(0).getStudyStatus()).isEqualTo(StudyStatus.STUDYING);
        assertThat(roadmapDto.getCourseList().get(0).getCourses().get(0).getOrder()).isEqualTo(1);
        assertThat(roadmapDto.getCourseList().get(1).getIndex()).isEqualTo(1);
        assertThat(roadmapDto.getCourseList().get(1).getCourses().get(0).getId()).isEqualTo(2L);
        assertThat(roadmapDto.getCourseList().get(1).getCourses().get(0).getName()).isEqualTo("SKILL1");
        assertThat(roadmapDto.getCourseList().get(1).getCourses().get(0).getType()).isEqualTo(CourseType.SKILL);
        assertThat(roadmapDto.getCourseList().get(1).getCourses().get(0).getMatchingFlag()).isEqualTo(MatchingFlag.YES);
        assertThat(roadmapDto.getCourseList().get(1).getCourses().get(0).getStudyStatus()).isNull();
        assertThat(roadmapDto.getCourseList().get(1).getCourses().get(0).getOrder()).isEqualTo(2);
    }

    @DisplayName("코스 목록의 첫번째가 SKILL이면 맨 앞에 빈 목록을 추가한다.")
    @Test
    void getRoadmap_success_test_if_first_is_skill_add_empty_list() {
        // given
        Long jobId = 1L;
        Long detailedPositionId = 1L;
        LoginUser loginUser = LoginUser.builder().user(User.builder().id(1L).build()).build();

        // stub
        RecruitmentInfo recruitmentInfo = makeRecruitmentInfo();
        when(recruitmentInfoService.findRecruitmentInfo(anyLong(), anyLong())).thenReturn(Optional.of(recruitmentInfo));

        Course course1 = Course.builder().id(1L).name("SKILL1").type(CourseType.SKILL).order(1).job(recruitmentInfo.getJob()).build();
        List<RoadmapAndStatusDto> roadmapAndStatusDtoList = new ArrayList<>();
        roadmapAndStatusDtoList.add(RoadmapAndStatusDto.builder().matchingFlag(MatchingFlag.YES).course(course1).studyStatus(null).build());
        when(roadmapRepository.getRoadmapListByRecruitmentInfoIdAndUserId(anyLong(), anyLong())).thenReturn(roadmapAndStatusDtoList);

        // when
        RoadmapDto roadmapDto = roadmapService.getRoadmap(jobId, detailedPositionId, loginUser);

        // then
        assertThat(roadmapDto.getCourseList().size()).isEqualTo(2);
        assertThat(roadmapDto.getCourseList().get(0).getIndex()).isEqualTo(0);
        assertThat(roadmapDto.getCourseList().get(0).getCourses()).isEmpty();
        assertThat(roadmapDto.getCourseList().get(1).getIndex()).isEqualTo(1);
        assertThat(roadmapDto.getCourseList().get(1).getCourses().size()).isEqualTo(1);
        assertThat(roadmapDto.getCourseList().get(1).getCourses().get(0).getId()).isEqualTo(1L);
        assertThat(roadmapDto.getCourseList().get(1).getCourses().get(0).getName()).isEqualTo("SKILL1");
        assertThat(roadmapDto.getCourseList().get(1).getCourses().get(0).getType()).isEqualTo(CourseType.SKILL);
    }

    @DisplayName("코스 목록의 첫번째가 CS면 맨 앞에 빈 목록을 추가하지 않는다.")
    @Test
    void getRoadmap_success_test_if_first_is_cs_not_add_empty_list() {
        // given
        Long jobId = 1L;
        Long detailedPositionId = 1L;
        LoginUser loginUser = LoginUser.builder().user(User.builder().id(1L).build()).build();

        // stub
        RecruitmentInfo recruitmentInfo = makeRecruitmentInfo();
        when(recruitmentInfoService.findRecruitmentInfo(anyLong(), anyLong())).thenReturn(Optional.of(recruitmentInfo));

        Course course1 = Course.builder().id(1L).name("CS1").type(CourseType.CS).order(1).job(recruitmentInfo.getJob()).build();
        List<RoadmapAndStatusDto> roadmapAndStatusDtoList = new ArrayList<>();
        roadmapAndStatusDtoList.add(RoadmapAndStatusDto.builder().matchingFlag(MatchingFlag.YES).course(course1).build());
        when(roadmapRepository.getRoadmapListByRecruitmentInfoIdAndUserId(anyLong(), anyLong())).thenReturn(roadmapAndStatusDtoList);

        // when
        RoadmapDto roadmapDto = roadmapService.getRoadmap(jobId, detailedPositionId, loginUser);

        // then
        assertThat(roadmapDto.getCourseList().size()).isEqualTo(1);
        assertThat(roadmapDto.getCourseList().get(0).getIndex()).isEqualTo(0);
        assertThat(roadmapDto.getCourseList().get(0).getCourses().size()).isEqualTo(1);
        assertThat(roadmapDto.getCourseList().get(0).getCourses().get(0).getId()).isEqualTo(1L);
        assertThat(roadmapDto.getCourseList().get(0).getCourses().get(0).getName()).isEqualTo("CS1");
        assertThat(roadmapDto.getCourseList().get(0).getCourses().get(0).getType()).isEqualTo(CourseType.CS);
    }

    @DisplayName("SKILL 다음에 SKILL이 나오면 중간에 빈 목록을 추가한다.")
    @Test
    void getRoadmap_success_test_if_skill_between_skill_add_empty_list() {
        // given
        Long jobId = 1L;
        Long detailedPositionId = 1L;
        LoginUser loginUser = LoginUser.builder().user(User.builder().id(1L).build()).build();

        // stub
        RecruitmentInfo recruitmentInfo = makeRecruitmentInfo();
        when(recruitmentInfoService.findRecruitmentInfo(anyLong(), anyLong())).thenReturn(Optional.of(recruitmentInfo));

        Course course1 = Course.builder().id(1L).name("CS1").type(CourseType.CS).order(1).job(recruitmentInfo.getJob()).build();
        Course course2 = Course.builder().id(2L).name("SKILL1").type(CourseType.SKILL).order(2).job(recruitmentInfo.getJob()).build();
        Course course3 = Course.builder().id(3L).name("SKILL2").type(CourseType.SKILL).order(4).job(recruitmentInfo.getJob()).build();
        List<RoadmapAndStatusDto> roadmapAndStatusDtoList = new ArrayList<>();
        roadmapAndStatusDtoList.add(RoadmapAndStatusDto.builder().matchingFlag(MatchingFlag.YES).course(course1).build());
        roadmapAndStatusDtoList.add(RoadmapAndStatusDto.builder().matchingFlag(MatchingFlag.YES).course(course2).build());
        roadmapAndStatusDtoList.add(RoadmapAndStatusDto.builder().matchingFlag(MatchingFlag.YES).course(course3).build());
        when(roadmapRepository.getRoadmapListByRecruitmentInfoIdAndUserId(anyLong(), anyLong())).thenReturn(roadmapAndStatusDtoList);

        // when
        RoadmapDto roadmapDto = roadmapService.getRoadmap(jobId, detailedPositionId, loginUser);

        // then
        assertThat(roadmapDto.getCourseList().size()).isEqualTo(4);
        assertThat(roadmapDto.getCourseList().get(0).getCourses().size()).isEqualTo(1);
        assertThat(roadmapDto.getCourseList().get(0).getCourses().get(0).getName()).isEqualTo("CS1");
        assertThat(roadmapDto.getCourseList().get(0).getCourses().get(0).getType()).isEqualTo(CourseType.CS);
        assertThat(roadmapDto.getCourseList().get(1).getCourses().size()).isEqualTo(1);
        assertThat(roadmapDto.getCourseList().get(1).getCourses().get(0).getName()).isEqualTo("SKILL1");
        assertThat(roadmapDto.getCourseList().get(1).getCourses().get(0).getType()).isEqualTo(CourseType.SKILL);
        assertThat(roadmapDto.getCourseList().get(2).getCourses()).isEmpty();
        assertThat(roadmapDto.getCourseList().get(3).getCourses().size()).isEqualTo(1);
        assertThat(roadmapDto.getCourseList().get(3).getCourses().get(0).getName()).isEqualTo("SKILL2");
        assertThat(roadmapDto.getCourseList().get(3).getCourses().get(0).getType()).isEqualTo(CourseType.SKILL);
    }

    @DisplayName("SKILL 다음 CS가 나오면 빈 목록을 추가하지 않는다.")
    @Test
    void getRoadmap_success_test_if_skill_between_cs_not_add_empty_list() {
        // given
        Long jobId = 1L;
        Long detailedPositionId = 1L;
        LoginUser loginUser = LoginUser.builder().user(User.builder().id(1L).build()).build();

        // stub
        RecruitmentInfo recruitmentInfo = makeRecruitmentInfo();
        when(recruitmentInfoService.findRecruitmentInfo(anyLong(), anyLong())).thenReturn(Optional.of(recruitmentInfo));

        Course course1 = Course.builder().id(1L).name("CS1").type(CourseType.CS).order(1).job(recruitmentInfo.getJob()).build();
        Course course2 = Course.builder().id(2L).name("SKILL1").type(CourseType.SKILL).order(2).job(recruitmentInfo.getJob()).build();
        Course course3 = Course.builder().id(3L).name("CS2").type(CourseType.CS).order(4).job(recruitmentInfo.getJob()).build();
        List<RoadmapAndStatusDto> roadmapAndStatusDtoList = new ArrayList<>();
        roadmapAndStatusDtoList.add(RoadmapAndStatusDto.builder().matchingFlag(MatchingFlag.YES).course(course1).build());
        roadmapAndStatusDtoList.add(RoadmapAndStatusDto.builder().matchingFlag(MatchingFlag.YES).course(course2).build());
        roadmapAndStatusDtoList.add(RoadmapAndStatusDto.builder().matchingFlag(MatchingFlag.YES).course(course3).build());
        when(roadmapRepository.getRoadmapListByRecruitmentInfoIdAndUserId(anyLong(), anyLong())).thenReturn(roadmapAndStatusDtoList);

        // when
        RoadmapDto roadmapDto = roadmapService.getRoadmap(jobId, detailedPositionId, loginUser);

        // then
        assertThat(roadmapDto.getCourseList().size()).isEqualTo(3);
        assertThat(roadmapDto.getCourseList().get(0).getCourses().size()).isEqualTo(1);
        assertThat(roadmapDto.getCourseList().get(0).getCourses().get(0).getName()).isEqualTo("CS1");
        assertThat(roadmapDto.getCourseList().get(0).getCourses().get(0).getType()).isEqualTo(CourseType.CS);
        assertThat(roadmapDto.getCourseList().get(1).getCourses().get(0).getName()).isEqualTo("SKILL1");
        assertThat(roadmapDto.getCourseList().get(1).getCourses().get(0).getType()).isEqualTo(CourseType.SKILL);
        assertThat(roadmapDto.getCourseList().get(2).getCourses().get(0).getName()).isEqualTo("CS2");
        assertThat(roadmapDto.getCourseList().get(2).getCourses().get(0).getType()).isEqualTo(CourseType.CS);
    }

    @DisplayName("같은 순서를 가진 코스는 같은 목록에 포함된다")
    @Test
    public void getRoadmap_success_test_same_order_course_is_same_list() {
        // given
        Long jobId = 1L;
        Long detailedPositionId = 1L;
        LoginUser loginUser = LoginUser.builder().user(User.builder().id(1L).build()).build();

        // stub
        RecruitmentInfo recruitmentInfo = makeRecruitmentInfo();
        when(recruitmentInfoService.findRecruitmentInfo(anyLong(), anyLong())).thenReturn(Optional.of(recruitmentInfo));

        Course course1 = Course.builder().id(1L).name("CS1").type(CourseType.CS).order(1).job(recruitmentInfo.getJob()).build();
        Course course2 = Course.builder().id(2L).name("SKILL1").type(CourseType.SKILL).order(2).job(recruitmentInfo.getJob()).build();
        Course course3 = Course.builder().id(3L).name("CS2").type(CourseType.CS).order(2).job(recruitmentInfo.getJob()).build();
        List<RoadmapAndStatusDto> roadmapAndStatusDtoList = new ArrayList<>();
        roadmapAndStatusDtoList.add(RoadmapAndStatusDto.builder().matchingFlag(MatchingFlag.YES).course(course1).build());
        roadmapAndStatusDtoList.add(RoadmapAndStatusDto.builder().matchingFlag(MatchingFlag.YES).course(course2).build());
        roadmapAndStatusDtoList.add(RoadmapAndStatusDto.builder().matchingFlag(MatchingFlag.YES).course(course3).build());
        when(roadmapRepository.getRoadmapListByRecruitmentInfoIdAndUserId(anyLong(), anyLong())).thenReturn(roadmapAndStatusDtoList);

        // when
        RoadmapDto roadmapDto = roadmapService.getRoadmap(jobId, detailedPositionId, loginUser);

        // then
        assertThat(roadmapDto.getCourseList().size()).isEqualTo(2);
        assertThat(roadmapDto.getCourseList().get(0).getCourses().size()).isEqualTo(1);
        assertThat(roadmapDto.getCourseList().get(0).getCourses().get(0).getName()).isEqualTo("CS1");
        assertThat(roadmapDto.getCourseList().get(0).getCourses().get(0).getType()).isEqualTo(CourseType.CS);
        assertThat(roadmapDto.getCourseList().get(1).getCourses().size()).isEqualTo(2);
        assertThat(roadmapDto.getCourseList().get(1).getCourses().get(0).getName()).isEqualTo("SKILL1");
        assertThat(roadmapDto.getCourseList().get(1).getCourses().get(0).getType()).isEqualTo(CourseType.SKILL);
        assertThat(roadmapDto.getCourseList().get(1).getCourses().get(1).getName()).isEqualTo("CS2");
        assertThat(roadmapDto.getCourseList().get(1).getCourses().get(1).getType()).isEqualTo(CourseType.CS);
    }
    
    // 로그인 안했을떄
    
    @DisplayName("코스 목록을 가져올 때 채용정보를 찾을 수 없으면 예외를 발생시킨다.")
    @Test
    void getRoadmap_not_found_recruitment_info_test() {
        // given
        Long jobId = 1L;
        Long detailedPositionId = 1L;
        LoginUser loginUser = LoginUser.builder().user(User.builder().id(1L).build()).build();

        // stub
        when(recruitmentInfoService.findRecruitmentInfo(anyLong(), anyLong())).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> roadmapService.getRoadmap(jobId, detailedPositionId, loginUser))
                .isInstanceOf(RecruitmentInfoNotFoundException.class);
    }

    /**
     * 코스 상세 테스트
     */
    @DisplayName("코스 상세 목록과 회사 요구 스킬이 일치하는 항목이 있으면 그 항목만 가져온다.")
    @Test
    void getCourseDetails_success_test_match_course_detail_and_company_required_skill() {
        // given
        Long courseId = 1L;
        Long jobId = 1L;
        Long detailedPositionId = 1L;
        LoginUser loginUser = LoginUser.builder().user(User.builder().id(1L).build()).build();

        // stub
        RecruitmentInfo recruitmentInfo = makeRecruitmentInfo();
        when(recruitmentInfoService.findRecruitmentInfo(anyLong(), anyLong())).thenReturn(Optional.of(recruitmentInfo));

        Course course = Course.builder().id(1L).name("언어").type(CourseType.SKILL).build();
        Roadmap roadmap = Roadmap.builder().id(1L).course(course).recruitmentInfo(recruitmentInfo).build();
        when(roadmapRepository.findRoadmapWithCourseByCourseIdAndRecruitmentInfoId(anyLong(), anyLong())).thenReturn(Optional.of(roadmap));

        List<CourseDetail> filteredCourseDetailList = new ArrayList<>();
        filteredCourseDetailList.add(CourseDetail.builder().id(1L).name("C").build());
        filteredCourseDetailList.add(CourseDetail.builder().id(3L).name("Java").build());
        when(courseDetailRepository.getCourseDetailListWithAbilityByCourseIdOrderByName(anyLong(), anyLong())).thenReturn(filteredCourseDetailList);

        UserRoadmap userRoadmap = UserRoadmap.builder().id(1L).user(loginUser.getUser()).studyStatus(StudyStatus.STUDYING).roadmap(roadmap).build();
        when(userRoadmapRepository.findByUserIdAndRoadmapId(anyLong(), anyLong())).thenReturn(Optional.of(userRoadmap));

        // when
        CourseDetailResponseDto courseDetailResponseDto = roadmapService.getCourseDetails(courseId, jobId, detailedPositionId, loginUser);

        // then
        assertThat(courseDetailResponseDto.getCourseName()).isEqualTo("언어");
        assertThat(courseDetailResponseDto.getLoginStatus()).isEqualTo(LoginStatus.YES);
        assertThat(courseDetailResponseDto.getStudyStatus()).isEqualTo(StudyStatus.STUDYING);
        assertThat(courseDetailResponseDto.getCourseDetails().size()).isEqualTo(2);
        assertThat(courseDetailResponseDto.getCourseDetails().get(0).getId()).isEqualTo(1L);
        assertThat(courseDetailResponseDto.getCourseDetails().get(0).getName()).isEqualTo("C");
        assertThat(courseDetailResponseDto.getCourseDetails().get(1).getId()).isEqualTo(3L);
        assertThat(courseDetailResponseDto.getCourseDetails().get(1).getName()).isEqualTo("Java");
    }

    @DisplayName("코스 상세 목록과 회사 요구 스킬이 일치하는 항목이 없으면 모든 코스 상세 목록을 가져온다.")
    @Test
    void getCourseDetails_success_test_not_match_course_detail_and_company_required_skill() {
        // given
        Long courseId = 1L;
        Long jobId = 1L;
        Long detailedPositionId = 1L;
        LoginUser loginUser = LoginUser.builder().user(User.builder().id(1L).build()).build();

        // stub
        RecruitmentInfo recruitmentInfo = makeRecruitmentInfo();
        when(recruitmentInfoService.findRecruitmentInfo(anyLong(), anyLong())).thenReturn(Optional.of(recruitmentInfo));

        Course course = Course.builder().id(1L).name("언어").type(CourseType.SKILL).build();
        Roadmap roadmap = Roadmap.builder().id(1L).course(course).recruitmentInfo(recruitmentInfo).build();
        when(roadmapRepository.findRoadmapWithCourseByCourseIdAndRecruitmentInfoId(anyLong(), anyLong())).thenReturn(Optional.of(roadmap));

        List<CourseDetail> emptyCourseDetailList = new ArrayList<>();
        when(courseDetailRepository.getCourseDetailListWithAbilityByCourseIdOrderByName(anyLong(), anyLong())).thenReturn(emptyCourseDetailList);

        List<CourseDetail> filteredCourseDetailList = new ArrayList<>();
        filteredCourseDetailList.add(CourseDetail.builder().id(1L).name("C").build());
        filteredCourseDetailList.add(CourseDetail.builder().id(2L).name("C++").build());
        filteredCourseDetailList.add(CourseDetail.builder().id(3L).name("Java").build());
        filteredCourseDetailList.add(CourseDetail.builder().id(4L).name("Kotlin").build());
        when(courseDetailRepository.getAllCourseDetailByCourseIdOrderByName(anyLong())).thenReturn(filteredCourseDetailList);

        UserRoadmap userRoadmap = UserRoadmap.builder().id(1L).user(loginUser.getUser()).roadmap(roadmap).build();
        when(userRoadmapRepository.findByUserIdAndRoadmapId(anyLong(), anyLong())).thenReturn(Optional.of(userRoadmap));

        // when
        CourseDetailResponseDto courseDetailResponseDto = roadmapService.getCourseDetails(courseId, jobId, detailedPositionId, loginUser);

        // then
        assertThat(courseDetailResponseDto.getCourseName()).isEqualTo("언어");
        assertThat(courseDetailResponseDto.getLoginStatus()).isEqualTo(LoginStatus.YES);
        assertThat(courseDetailResponseDto.getStudyStatus()).isNull();
        assertThat(courseDetailResponseDto.getCourseDetails().size()).isEqualTo(4);
        assertThat(courseDetailResponseDto.getCourseDetails().get(0).getId()).isEqualTo(1L);
        assertThat(courseDetailResponseDto.getCourseDetails().get(0).getName()).isEqualTo("C");
        assertThat(courseDetailResponseDto.getCourseDetails().get(1).getId()).isEqualTo(2L);
        assertThat(courseDetailResponseDto.getCourseDetails().get(1).getName()).isEqualTo("C++");
        assertThat(courseDetailResponseDto.getCourseDetails().get(2).getId()).isEqualTo(3L);
        assertThat(courseDetailResponseDto.getCourseDetails().get(2).getName()).isEqualTo("Java");
        assertThat(courseDetailResponseDto.getCourseDetails().get(3).getId()).isEqualTo(4L);
        assertThat(courseDetailResponseDto.getCourseDetails().get(3).getName()).isEqualTo("Kotlin");
    }

    @DisplayName("로그인 안한 유저가 허용된 코스 상세 목록 요청시 성공적으로 가져온다.")
    @Test
    void getCourseDetails_success_test_with_not_login_user() {
        // given
        Long courseId = 1L;
        Long jobId = 1L;
        Long detailedPositionId = 1L;

        // stub
        RecruitmentInfo recruitmentInfo = makeRecruitmentInfo();
        when(recruitmentInfoService.findRecruitmentInfo(anyLong(), anyLong())).thenReturn(Optional.of(recruitmentInfo));

        Course course = Course.builder().id(1L).name("언어").type(CourseType.SKILL).build();
        Roadmap roadmap = Roadmap.builder().id(1L).course(course).recruitmentInfo(recruitmentInfo).build();
        when(roadmapRepository.findRoadmapWithCourseByCourseIdAndRecruitmentInfoId(anyLong(), anyLong())).thenReturn(Optional.of(roadmap));

        when(roadmapRepository.findTop2ByRecruitmentInfoIdOrderByCourseOrder(anyLong())).thenReturn(List.of(roadmap));

        List<CourseDetail> filteredCourseDetailList = new ArrayList<>();
        filteredCourseDetailList.add(CourseDetail.builder().id(1L).name("C").build());
        filteredCourseDetailList.add(CourseDetail.builder().id(3L).name("Java").build());
        when(courseDetailRepository.getCourseDetailListWithAbilityByCourseIdOrderByName(anyLong(), anyLong())).thenReturn(filteredCourseDetailList);

        // when
        CourseDetailResponseDto courseDetailResponseDto = roadmapService.getCourseDetails(courseId, jobId, detailedPositionId, null);

        // then
        assertThat(courseDetailResponseDto.getCourseName()).isEqualTo("언어");
        assertThat(courseDetailResponseDto.getLoginStatus()).isEqualTo(LoginStatus.NO);
        assertThat(courseDetailResponseDto.getStudyStatus()).isNull();
        assertThat(courseDetailResponseDto.getCourseDetails().size()).isEqualTo(2);
        assertThat(courseDetailResponseDto.getCourseDetails().get(0).getId()).isEqualTo(1L);
        assertThat(courseDetailResponseDto.getCourseDetails().get(0).getName()).isEqualTo("C");
        assertThat(courseDetailResponseDto.getCourseDetails().get(1).getId()).isEqualTo(3L);
        assertThat(courseDetailResponseDto.getCourseDetails().get(1).getName()).isEqualTo("Java");
    }

    @DisplayName("로그인 안한 유저가 허용되지 않은 코스 상세 목록 요청시 예외를 발생시킨다.")
    @Test
    void getCourseDetails_throw_exception_with_not_login_user() {
        // given
        Long courseId = 1L;
        Long jobId = 1L;
        Long detailedPositionId = 1L;

        // stub
        RecruitmentInfo recruitmentInfo = makeRecruitmentInfo();
        when(recruitmentInfoService.findRecruitmentInfo(anyLong(), anyLong())).thenReturn(Optional.of(recruitmentInfo));

        Course course3 = Course.builder().id(3L).name("네트워크").type(CourseType.CS).build();
        Roadmap notAllowRoadmap = Roadmap.builder().id(3L).course(course3).recruitmentInfo(recruitmentInfo).build();
        when(roadmapRepository.findRoadmapWithCourseByCourseIdAndRecruitmentInfoId(anyLong(), anyLong())).thenReturn(Optional.of(notAllowRoadmap));

        Course course1 = Course.builder().id(1L).name("언어").type(CourseType.SKILL).build();
        Roadmap allowRoadmap1 = Roadmap.builder().id(1L).course(course1).recruitmentInfo(recruitmentInfo).build();
        Course course2 = Course.builder().id(2L).name("프레임 워크").type(CourseType.SKILL).build();
        Roadmap allowRoadmap2 = Roadmap.builder().id(2L).course(course2).recruitmentInfo(recruitmentInfo).build();
        when(roadmapRepository.findTop2ByRecruitmentInfoIdOrderByCourseOrder(anyLong())).thenReturn(List.of(allowRoadmap1, allowRoadmap2));

        // when & then
        assertThatThrownBy(() -> roadmapService.getCourseDetails(courseId, jobId, detailedPositionId, null))
                .isInstanceOf(UnAuthorizedCourseAccessException.class);
    }

    @DisplayName("코스 상세 목록을 가져올 때 채용정보를 찾을 수 없으면 예외를 발생시킨다.")
    @Test
    void getCourseDetails_throw_exception_if_not_found_recruitment_info() {
        // given
        Long courseId = 1L;
        Long jobId = 1L;
        Long detailedPositionId = 1L;

        // stub
        when(recruitmentInfoService.findRecruitmentInfo(anyLong(), anyLong())).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> roadmapService.getCourseDetails(courseId, jobId, detailedPositionId, null))
                .isInstanceOf(RecruitmentInfoNotFoundException.class);
    }

    @DisplayName("코스 상세 목록을 가져올 때 채용정보와 코스에 맞는 로드맵을 찾을 수 없으면 예외를 발생시킨다.")
    @Test
    void getCourseDetails_throw_exception_if_not_found_roadmap() {
        // given
        Long courseId = 1L;
        Long jobId = 1L;
        Long detailedPositionId = 1L;

        // stub
        RecruitmentInfo recruitmentInfo = makeRecruitmentInfo();
        when(recruitmentInfoService.findRecruitmentInfo(anyLong(), anyLong())).thenReturn(Optional.of(recruitmentInfo));
        when(roadmapRepository.findRoadmapWithCourseByCourseIdAndRecruitmentInfoId(anyLong(), anyLong())).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> roadmapService.getCourseDetails(courseId, jobId, detailedPositionId, null))
                .isInstanceOf(RoadmapNotMatchCourseAndCompanyInfoException.class);
    }

    /**
     * 코스 상세에 맞는 영상과 책 목록 가져오기 테스트
     */
    @DisplayName("코스 상세에 맞는 영상과 책 목록을 가져온다.")
    @Test
    void getCourseVideoAndBookList_success_test() {
        Long courseId = 1L;
        Long courseDetailId = 1L;
        Long jobId = 1L;
        Long detailedPositionId = 1L;
        LoginUser loginUser = LoginUser.builder().user(User.builder().id(1L).build()).build();

        // stub
        Course course = Course.builder().id(1L).name("언어").type(CourseType.SKILL).build();
        CourseDetail courseDetail = CourseDetail.builder().id(1L).name("C").description("C언어 설명").build();
        CourseToDetail courseToDetail = CourseToDetail.builder().course(course).courseDetail(courseDetail).build();
        when(courseToDetailRepository.findFetchByCourseIdAndCourseDetailId(anyLong(), anyLong())).thenReturn(Optional.of(courseToDetail));

        RecruitmentInfo recruitmentInfo = makeRecruitmentInfo();
        when(recruitmentInfoService.findRecruitmentInfo(anyLong(), anyLong())).thenReturn(Optional.of(recruitmentInfo));

        when(educationMaterialService.getCourseVideos(anyLong(), anyLong())).thenReturn(makeCourseVideoList());

        when(educationMaterialService.getCourseBooks(anyLong())).thenReturn(makeCourseBookList());

        // when
        CourseItemResponseDto result = roadmapService.getCourseVideoAndBookList(courseId, courseDetailId, jobId, detailedPositionId, loginUser);
        
        // then
        assertThat(result.getCourseTitle()).isEqualTo("언어");
        assertThat(result.getCourseDetailTitle()).isEqualTo("C");
        assertThat(result.getCourseDetailDescription()).isEqualTo("C언어 설명");
        assertThat(result.getCourseVideos().size()).isEqualTo(2);
        assertThat(result.getCourseVideos().get(0).getId()).isEqualTo(1L);
        assertThat(result.getCourseVideos().get(0).getTitle()).isEqualTo("test video");
        assertThat(result.getCourseVideos().get(0).getThumbnail()).isEqualTo("test thumbnail");
        assertThat(result.getCourseVideos().get(0).getUrl()).isEqualTo("test url");
        assertThat(result.getCourseVideos().get(0).getSource()).isEqualTo(VideoSource.YOUTUBE);
        assertThat(result.getCourseVideos().get(0).getLikeCnt()).isEqualTo(10L);
        assertThat(result.getCourseVideos().get(1).getId()).isEqualTo(2L);
        assertThat(result.getCourseVideos().get(1).getTitle()).isEqualTo("test video2");
        assertThat(result.getCourseVideos().get(1).getThumbnail()).isEqualTo("test thumbnail2");
        assertThat(result.getCourseVideos().get(1).getUrl()).isEqualTo("test url2");
        assertThat(result.getCourseVideos().get(1).getSource()).isEqualTo(VideoSource.YOUTUBE);
        assertThat(result.getCourseVideos().get(1).getLikeCnt()).isEqualTo(20L);
        assertThat(result.getCourseBooks().size()).isEqualTo(2);
        assertThat(result.getCourseBooks().get(0).getId()).isEqualTo(1L);
        assertThat(result.getCourseBooks().get(0).getTitle()).isEqualTo("test book");
        assertThat(result.getCourseBooks().get(0).getSource()).isEqualTo(BookSource.COUPANG);
        assertThat(result.getCourseBooks().get(0).getThumbnail()).isEqualTo("test thumbnail");
        assertThat(result.getCourseBooks().get(0).getUrl()).isEqualTo("test url");
        assertThat(result.getCourseBooks().get(1).getId()).isEqualTo(2L);
        assertThat(result.getCourseBooks().get(1).getTitle()).isEqualTo("test book2");
        assertThat(result.getCourseBooks().get(1).getSource()).isEqualTo(BookSource.COUPANG);
        assertThat(result.getCourseBooks().get(1).getThumbnail()).isEqualTo("test thumbnail2");
        assertThat(result.getCourseBooks().get(1).getUrl()).isEqualTo("test url2");
    }

    @DisplayName("로그인 안한 유저가 허용된 코스 상세에 맞는 영상과 책 목록 요청시 성공적으로 가져온다.")
    @Test
    void getCourseVideoAndBookList_success_test_with_not_login_user() {
        // given
        Long courseId = 1L;
        Long courseDetailId = 1L;
        Long jobId = 1L;
        Long detailedPositionId = 1L;

        // stub
        Course course = Course.builder().id(1L).name("언어").type(CourseType.SKILL).build();
        CourseDetail courseDetail = CourseDetail.builder().id(1L).name("C").description("C언어 설명").build();
        CourseToDetail courseToDetail = CourseToDetail.builder().course(course).courseDetail(courseDetail).build();
        when(courseToDetailRepository.findFetchByCourseIdAndCourseDetailId(anyLong(), anyLong())).thenReturn(Optional.of(courseToDetail));

        RecruitmentInfo recruitmentInfo = makeRecruitmentInfo();
        when(recruitmentInfoService.findRecruitmentInfo(anyLong(), anyLong())).thenReturn(Optional.of(recruitmentInfo));

        Roadmap roadmap = Roadmap.builder().id(1L).course(course).recruitmentInfo(recruitmentInfo).build();
        when(roadmapRepository.findTop2ByRecruitmentInfoIdOrderByCourseOrder(anyLong())).thenReturn(List.of(roadmap));

        when(educationMaterialService.getCourseVideos(anyLong(), any())).thenReturn(makeCourseVideoList());

        when(educationMaterialService.getCourseBooks(anyLong())).thenReturn(makeCourseBookList());

        // when
        CourseItemResponseDto result = roadmapService.getCourseVideoAndBookList(courseId, courseDetailId, jobId, detailedPositionId, null);

        // then
        assertThat(result.getCourseTitle()).isEqualTo("언어");
        assertThat(result.getCourseDetailTitle()).isEqualTo("C");
        assertThat(result.getCourseDetailDescription()).isEqualTo("C언어 설명");
        assertThat(result.getCourseVideos().size()).isEqualTo(2);
        assertThat(result.getCourseBooks().size()).isEqualTo(2);
    }

    @DisplayName("로그인 안한 유저가 허용되지 않은 코스 상세에 맞는 영상과 책 목록 요청시 예외를 발생시킨다.")
    @Test
    void getCourseVideoAndBookList_throw_exception_with_not_login_user() {
        // given
        Long courseId = 3L;
        Long courseDetailId = 1L;
        Long jobId = 1L;
        Long detailedPositionId = 1L;

        // stub
        Course course3 = Course.builder().id(3L).name("네트워크").type(CourseType.CS).build();
        CourseDetail courseDetail = CourseDetail.builder().id(4L).name("기초").description("네트워크 기초").build();
        CourseToDetail courseToDetail = CourseToDetail.builder().course(course3).courseDetail(courseDetail).build();
        when(courseToDetailRepository.findFetchByCourseIdAndCourseDetailId(anyLong(), anyLong())).thenReturn(Optional.of(courseToDetail));

        RecruitmentInfo recruitmentInfo = makeRecruitmentInfo();
        when(recruitmentInfoService.findRecruitmentInfo(anyLong(), anyLong())).thenReturn(Optional.of(recruitmentInfo));

        Course course1 = Course.builder().id(1L).name("언어").type(CourseType.SKILL).build();
        Roadmap allowRoadmap1 = Roadmap.builder().id(1L).course(course1).recruitmentInfo(recruitmentInfo).build();
        Course course2 = Course.builder().id(2L).name("프레임 워크").type(CourseType.SKILL).build();
        Roadmap allowRoadmap2 = Roadmap.builder().id(2L).course(course2).recruitmentInfo(recruitmentInfo).build();
        when(roadmapRepository.findTop2ByRecruitmentInfoIdOrderByCourseOrder(anyLong())).thenReturn(List.of(allowRoadmap1, allowRoadmap2));

        // when & then
        assertThatThrownBy(() -> roadmapService.getCourseVideoAndBookList(courseId, courseDetailId, jobId, detailedPositionId, null))
                .isInstanceOf(UnAuthorizedCourseAccessException.class);
    }

    @DisplayName("코스 상세에 맞는 영상과 책 목록을 가져올 때 코스 상세를 찾을 수 없으면 예외를 발생시킨다.")
    @Test
    void getCourseVideoAndBookList_throw_exception_if_not_found_course_detail() {
        // given
        Long courseId = 1L;
        Long courseDetailId = 1L;
        Long jobId = 1L;
        Long detailedPositionId = 1L;

        // stub
        when(courseToDetailRepository.findFetchByCourseIdAndCourseDetailId(anyLong(), anyLong())).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> roadmapService.getCourseVideoAndBookList(courseId, courseDetailId, jobId, detailedPositionId, null))
                .isInstanceOf(CourseDetailNotFoundException.class);
    }

    @DisplayName("코스 상세에 맞는 영상과 책 목록을 가져올 때 채용정보를 찾을 수 없으면 예외를 발생시킨다.")
    @Test
    void getCourseVideoAndBookList_throw_exception_if_not_found_recruitment_info() {
        // given
        Long courseId = 1L;
        Long courseDetailId = 1L;
        Long jobId = 1L;
        Long detailedPositionId = 1L;

        // stub
        Course course3 = Course.builder().id(3L).name("네트워크").type(CourseType.CS).build();
        CourseDetail courseDetail = CourseDetail.builder().id(4L).name("기초").description("네트워크 기초").build();
        CourseToDetail courseToDetail = CourseToDetail.builder().course(course3).courseDetail(courseDetail).build();
        when(courseToDetailRepository.findFetchByCourseIdAndCourseDetailId(anyLong(), anyLong())).thenReturn(Optional.of(courseToDetail));

        when(recruitmentInfoService.findRecruitmentInfo(anyLong(), anyLong())).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> roadmapService.getCourseVideoAndBookList(courseId, courseDetailId, jobId, detailedPositionId, null))
                .isInstanceOf(RecruitmentInfoNotFoundException.class);
    }

    // TODO. 학습 상태 변경 테스트 필요

    private RecruitmentInfo makeRecruitmentInfo() {
        Company company = Company.builder().id(1L).name("test company").build();
        Job job = Job.builder().id(1L).name("test job").build();
        DetailedPosition detailedPosition = DetailedPosition.builder().id(1L).name("test detailed position").company(company).build();
        return RecruitmentInfo.builder().id(1L).content("companyInfoUrl").company(company).job(job).detailedPosition(detailedPosition).build();
    }

    private List<CourseVideoWithLikeDto> makeCourseVideoList() {
        List<CourseVideoWithLikeDto> courseVideoList = new ArrayList<>();
        courseVideoList.add(CourseVideoWithLikeDto.builder().id(1L).title("test video").thumbnail("test thumbnail").url("test url").source(VideoSource.YOUTUBE)
                .courseVideoUserId(1L).likeCnt(10L).build());
        courseVideoList.add(CourseVideoWithLikeDto.builder().id(2L).title("test video2").thumbnail("test thumbnail2").url("test url2").source(VideoSource.YOUTUBE)
                .courseVideoUserId(2L).likeCnt(20L).build());
        return courseVideoList;
    }

    private List<CourseBook> makeCourseBookList() {
        List<CourseBook> courseBookList = new ArrayList<>();
        courseBookList.add(CourseBook.builder().id(1L).title("test book").source(BookSource.COUPANG).thumbnail("test thumbnail").url("test url").language(BookLanguage.KOR).build());
        courseBookList.add(CourseBook.builder().id(2L).title("test book2").source(BookSource.COUPANG).thumbnail("test thumbnail2").url("test url2").language(BookLanguage.ENG).build());
        return courseBookList;
    }
}