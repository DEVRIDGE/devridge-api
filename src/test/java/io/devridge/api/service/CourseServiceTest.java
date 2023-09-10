package io.devridge.api.service;

import io.devridge.api.domain.companyinfo.*;
import io.devridge.api.domain.roadmap.*;
import io.devridge.api.dto.CourseDetailResponseDto;
import io.devridge.api.dto.course.CourseListResponseDto;
import io.devridge.api.handler.ex.CourseNotFoundException;
import io.devridge.api.handler.ex.NotFoundCompanyInfo;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

    @InjectMocks
    private CourseService courseService;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private CompanyJobRepository companyJobRepository;

    @Mock
    private CompanyInfoRepository companyInfoRepository;

    @Mock
    private CourseDetailRepository courseDetailRepository;

    @Mock
    private RoadmapRepository roadmapRepository;

    @DisplayName("회사, 직무, 상세 직무를 바탕으로 회사 정보를 찾고 이를 바탕으로 코스 목록을 만들어서 전달한다")
    @Test
    public void get_course_list_success_test() {
        // given
        CompanyInfo companyInfo = makeCompanyInfo();
        List<Roadmap> roadmapList = new ArrayList<>();

        // stub
        when(companyInfoRepository.findCompanyInfoByCompanyIdAndJobIdAndDetailedPositionId(anyLong(), anyLong(), anyLong())).thenReturn(Optional.of(companyInfo));
        when(roadmapRepository.getRoadmapsIncludingCoursesByCompanyInfoId(anyLong())).thenReturn(roadmapList);

        // when
        CourseListResponseDto courseListResponseDto = courseService.getCourseList(1L, 1L, 1L);

        // then
        assertThat(courseListResponseDto.getCompanyName()).isEqualTo("test company");
        assertThat(courseListResponseDto.getJobName()).isEqualTo("test job");
        assertThat(courseListResponseDto.getCourseList().size()).isEqualTo(0);
    }

    @DisplayName("코스 목록을 만들 때 맨 처음 SKILL 타입이 나오면 빈 리스트를 추가한다.")
    @Test
    public void get_course_list_first_skill_success_test() {
        // given
        CompanyInfo companyInfo = makeCompanyInfo();
        List<Roadmap> roadmapList = new ArrayList<>();

        Course course1 = Course.builder().id(1L).name("SKILL1").type(CourseType.SKILL).order(2).job(companyInfo.getJob()).build();
        roadmapList.add(Roadmap.builder().course(course1).companyInfo(companyInfo).matchingFlag(MatchingStatus.NO).build());

        // stub
        when(companyInfoRepository.findCompanyInfoByCompanyIdAndJobIdAndDetailedPositionId(anyLong(), anyLong(), anyLong())).thenReturn(Optional.of(companyInfo));
        when(roadmapRepository.getRoadmapsIncludingCoursesByCompanyInfoId(anyLong())).thenReturn(roadmapList);

        // when
        CourseListResponseDto courseListResponseDto = courseService.getCourseList(1L, 1L, 1L);

        // then
        assertThat(courseListResponseDto.getCourseList().size()).isEqualTo(2);
        assertThat(courseListResponseDto.getCourseList().get(0).getIndex()).isEqualTo(0);
        assertThat(courseListResponseDto.getCourseList().get(0).getCourses()).isEmpty();
        assertThat(courseListResponseDto.getCourseList().get(1).getIndex()).isEqualTo(1);
        assertThat(courseListResponseDto.getCourseList().get(1).getCourses().size()).isEqualTo(1);
        assertThat(courseListResponseDto.getCourseList().get(1).getCourses().get(0).getId()).isEqualTo(1L);
        assertThat(courseListResponseDto.getCourseList().get(1).getCourses().get(0).getName()).isEqualTo("SKILL1");
        assertThat(courseListResponseDto.getCourseList().get(1).getCourses().get(0).getType()).isEqualTo(CourseType.SKILL);
        assertThat(courseListResponseDto.getCourseList().get(1).getCourses().get(0).getMatchingFlag()).isEqualTo(MatchingStatus.NO);
        assertThat(courseListResponseDto.getCourseList().get(1).getCourses().get(0).getOrder()).isEqualTo(2);
    }

    @DisplayName("코스 목록을 만들 때 맨 처음 CS 타입이 나오면 빈 리스트를 추가하지 않는다.")
    @Test
    public void get_course_list_first_cs_success_test() {
        // given
        CompanyInfo companyInfo = makeCompanyInfo();
        List<Roadmap> roadmapList = new ArrayList<>();

        Course course1 = Course.builder().id(1L).name("CS1").type(CourseType.CS).order(1).job(companyInfo.getJob()).build();
        roadmapList.add(Roadmap.builder().course(course1).companyInfo(companyInfo).matchingFlag(MatchingStatus.YES).build());

        // stub
        when(companyInfoRepository.findCompanyInfoByCompanyIdAndJobIdAndDetailedPositionId(anyLong(), anyLong(), anyLong())).thenReturn(Optional.of(companyInfo));
        when(roadmapRepository.getRoadmapsIncludingCoursesByCompanyInfoId(anyLong())).thenReturn(roadmapList);

        // when
        CourseListResponseDto courseListResponseDto = courseService.getCourseList(1L, 1L, 1L);

        // then
        assertThat(courseListResponseDto.getCourseList().size()).isEqualTo(1);
        assertThat(courseListResponseDto.getCourseList().get(0).getIndex()).isEqualTo(0);
        assertThat(courseListResponseDto.getCourseList().get(0).getCourses().size()).isEqualTo(1);
        assertThat(courseListResponseDto.getCourseList().get(0).getCourses().get(0).getId()).isEqualTo(1L);
        assertThat(courseListResponseDto.getCourseList().get(0).getCourses().get(0).getName()).isEqualTo("CS1");
        assertThat(courseListResponseDto.getCourseList().get(0).getCourses().get(0).getType()).isEqualTo(CourseType.CS);
        assertThat(courseListResponseDto.getCourseList().get(0).getCourses().get(0).getMatchingFlag()).isEqualTo(MatchingStatus.YES);
        assertThat(courseListResponseDto.getCourseList().get(0).getCourses().get(0).getOrder()).isEqualTo(1);
    }

    @DisplayName("SKILL 다음에 SKILL이 나오면 중간에 빈 리스트를 추가한다.")
    @Test
    public void get_course_list_skill_between_skill_success_test() {
        // given
        CompanyInfo companyInfo = makeCompanyInfo();
        List<Roadmap> roadmapList = new ArrayList<>();

        Course course1 = Course.builder().id(1L).name("CS1").type(CourseType.CS).order(1).job(companyInfo.getJob()).build();
        Course course2 = Course.builder().id(2L).name("SKILL1").type(CourseType.SKILL).order(2).job(companyInfo.getJob()).build();
        Course course3 = Course.builder().id(3L).name("SKILL2").type(CourseType.SKILL).order(4).job(companyInfo.getJob()).build();
        roadmapList.add(Roadmap.builder().course(course1).companyInfo(companyInfo).matchingFlag(MatchingStatus.NO).build());
        roadmapList.add(Roadmap.builder().course(course2).companyInfo(companyInfo).matchingFlag(MatchingStatus.YES).build());
        roadmapList.add(Roadmap.builder().course(course3).companyInfo(companyInfo).matchingFlag(MatchingStatus.YES).build());

        // stub
        when(companyInfoRepository.findCompanyInfoByCompanyIdAndJobIdAndDetailedPositionId(anyLong(), anyLong(), anyLong())).thenReturn(Optional.of(companyInfo));
        when(roadmapRepository.getRoadmapsIncludingCoursesByCompanyInfoId(anyLong())).thenReturn(roadmapList);

        // when
        CourseListResponseDto courseListResponseDto = courseService.getCourseList(1L, 1L, 1L);

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

    @DisplayName("SKILL 다음에 CS가 나오면 빈 리스트를 추가 하지 않는다.")
    @Test
    public void get_course_list_skill_between_cs_success_test() {
        // given
        CompanyInfo companyInfo = makeCompanyInfo();
        List<Roadmap> roadmapList = new ArrayList<>();

        Course course1 = Course.builder().id(1L).name("CS1").type(CourseType.CS).order(1).job(companyInfo.getJob()).build();
        Course course2 = Course.builder().id(2L).name("SKILL1").type(CourseType.SKILL).order(2).job(companyInfo.getJob()).build();
        Course course3 = Course.builder().id(3L).name("CS2").type(CourseType.CS).order(4).job(companyInfo.getJob()).build();
        roadmapList.add(Roadmap.builder().course(course1).companyInfo(companyInfo).matchingFlag(MatchingStatus.NO).build());
        roadmapList.add(Roadmap.builder().course(course2).companyInfo(companyInfo).matchingFlag(MatchingStatus.YES).build());
        roadmapList.add(Roadmap.builder().course(course3).companyInfo(companyInfo).matchingFlag(MatchingStatus.YES).build());

        // stub
        when(companyInfoRepository.findCompanyInfoByCompanyIdAndJobIdAndDetailedPositionId(anyLong(), anyLong(), anyLong())).thenReturn(Optional.of(companyInfo));
        when(roadmapRepository.getRoadmapsIncludingCoursesByCompanyInfoId(anyLong())).thenReturn(roadmapList);

        // when
        CourseListResponseDto courseListResponseDto = courseService.getCourseList(1L, 1L, 1L);

        // then
        assertThat(courseListResponseDto.getCourseList().size()).isEqualTo(3);
        assertThat(courseListResponseDto.getCourseList().get(0).getCourses().get(0).getName()).isEqualTo("CS1");
        assertThat(courseListResponseDto.getCourseList().get(0).getCourses().get(0).getType()).isEqualTo(CourseType.CS);
        assertThat(courseListResponseDto.getCourseList().get(1).getCourses().get(0).getName()).isEqualTo("SKILL1");
        assertThat(courseListResponseDto.getCourseList().get(1).getCourses().get(0).getType()).isEqualTo(CourseType.SKILL);
        assertThat(courseListResponseDto.getCourseList().get(2).getCourses().get(0).getName()).isEqualTo("CS2");
        assertThat(courseListResponseDto.getCourseList().get(2).getCourses().get(0).getType()).isEqualTo(CourseType.CS);
    }

    @DisplayName("같은 순서를 가진 코스는 같은 리스트에 들어간다.")
    @Test
    public void same_order_course_is_same_list() {
        // given
        CompanyInfo companyInfo = makeCompanyInfo();
        List<Roadmap> roadmapList = new ArrayList<>();

        Course course1 = Course.builder().id(1L).name("CS1").type(CourseType.CS).order(1).job(companyInfo.getJob()).build();
        Course course2 = Course.builder().id(1L).name("CS2").type(CourseType.CS).order(1).job(companyInfo.getJob()).build();
        Course course3 = Course.builder().id(1L).name("CS3").type(CourseType.CS).order(1).job(companyInfo.getJob()).build();
        roadmapList.add(Roadmap.builder().course(course1).companyInfo(companyInfo).matchingFlag(MatchingStatus.NO).build());
        roadmapList.add(Roadmap.builder().course(course2).companyInfo(companyInfo).matchingFlag(MatchingStatus.YES).build());
        roadmapList.add(Roadmap.builder().course(course3).companyInfo(companyInfo).matchingFlag(MatchingStatus.YES).build());

        // stub
        when(companyInfoRepository.findCompanyInfoByCompanyIdAndJobIdAndDetailedPositionId(anyLong(), anyLong(), anyLong())).thenReturn(Optional.of(companyInfo));
        when(roadmapRepository.getRoadmapsIncludingCoursesByCompanyInfoId(anyLong())).thenReturn(roadmapList);

        // when
        CourseListResponseDto courseListResponseDto = courseService.getCourseList(1L, 1L, 1L);

        // then
        assertThat(courseListResponseDto.getCourseList().size()).isEqualTo(1);
        assertThat(courseListResponseDto.getCourseList().get(0).getCourses().get(0).getName()).isEqualTo("CS1");
        assertThat(courseListResponseDto.getCourseList().get(0).getCourses().get(0).getType()).isEqualTo(CourseType.CS);
        assertThat(courseListResponseDto.getCourseList().get(0).getCourses().get(1).getName()).isEqualTo("CS2");
        assertThat(courseListResponseDto.getCourseList().get(0).getCourses().get(1).getType()).isEqualTo(CourseType.CS);
        assertThat(courseListResponseDto.getCourseList().get(0).getCourses().get(2).getName()).isEqualTo("CS3");
        assertThat(courseListResponseDto.getCourseList().get(0).getCourses().get(2).getType()).isEqualTo(CourseType.CS);
    }

    @DisplayName("코스 리스트를 찾을 때 회사 정보를 찾을 수 없으면 예외를 전달한다")
    @Test
    public void course_list_not_found_company_info_fail_test() {
        // stub
        when(companyInfoRepository.findCompanyInfoByCompanyIdAndJobIdAndDetailedPositionId(anyLong(), anyLong(), anyLong())).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> courseService.getCourseList(1L, 1L, 1L))
                .isInstanceOf(NotFoundCompanyInfo.class);
    }

    @DisplayName("코스, 회사, 직무, 서비스가 주어지면 CourseDetail 리스트를 정상적으로 반환한다")
    @Test
    void getCourseDetailList_success_test() {
        //given
        Company company = Company.builder().id(1L).name("토스").build();
        Job job = Job.builder().id(1L).name("백엔드").build();
        DetailedPosition detailedPosition = DetailedPosition.builder().id(1L).name("Product").company(company).build();
        CompanyInfo companyInfo = CompanyInfo.builder().id(1L).job(job).detailedPosition(detailedPosition).company(company).build();

        Course course = Course.builder().id(1L).name("언어").job(job).build();

        //stub
        when(companyInfoRepository.findByCompanyIdAndJobIdAndDetailedPositionId(company.getId(), job.getId(), detailedPosition.getId())).thenReturn(Optional.of(companyInfo));
        when(courseRepository.findById(course.getId())).thenReturn(Optional.of(course));
        when(courseDetailRepository.getCourseDetailList(course.getId(), company.getId(), job.getId(), detailedPosition.getId())).thenReturn(makeCourseDetailList(course));

        //when
        CourseDetailResponseDto courseDetailResponseDto = courseService.getCourseDetailList(course.getId(), company.getId(), job.getId(), detailedPosition.getId());

        //then
        assertThat(courseDetailResponseDto.getCourseName()).isEqualTo(course.getName());
        assertThat(courseDetailResponseDto.getCourseDetails().size()).isEqualTo(2);
        assertThat(courseDetailResponseDto.getCourseDetails().get(0).getName()).isEqualTo("Java");
        assertThat(courseDetailResponseDto.getCourseDetails().get(1).getName()).isEqualTo("Kotlin");
    }

    @DisplayName("회사 또는 직무 또는 서비스종류가 잘못되면 에러를 발생시킨다")
    @Test
    void getCourseDetailList_not_existing_company_or_job_or_detailedPosition_fail_test() {
        //given
        long courseId = 1L;
        long wrongCompanyId = 9999L;
        long wrongJobId = 9999L;
        long wrongDetailedPositionId = 9999L;

        //stub
        when(companyInfoRepository.findByCompanyIdAndJobIdAndDetailedPositionId(wrongCompanyId, wrongJobId, wrongDetailedPositionId)).thenReturn(Optional.empty());

        //when & then
        assertThatThrownBy(() -> courseService.getCourseDetailList(courseId, wrongCompanyId, wrongJobId, wrongDetailedPositionId))
                .isInstanceOf(CompanyInfoNotFoundException.class);
    }

    @DisplayName("코스를 찾을 수 없으면 에러가 발생한다")
    @Test
    void getCourseDetailList_not_existing_course_fail_test() {
        //given
        Company company = Company.builder().id(1L).name("토스").build();
        Job job = Job.builder().id(1L).name("백엔드").build();
        DetailedPosition detailedPosition = DetailedPosition.builder().id(1L).name("Product").company(company).build();
        CompanyInfo companyInfo = CompanyInfo.builder().id(1L).company(company).job(job).detailedPosition(detailedPosition).build();

        Course course = Course.builder().id(1L).name("언어").job(job).build();

        //stub
        when(companyInfoRepository.findByCompanyIdAndJobIdAndDetailedPositionId(company.getId(), job.getId(), detailedPosition.getId())).thenReturn(Optional.of(companyInfo));
        when(courseRepository.findById(course.getId())).thenReturn(Optional.empty());

        //when & then
        assertThatThrownBy(() -> courseService.getCourseDetailList(course.getId(), company.getId(), job.getId(), detailedPosition.getId()))
                .isInstanceOf(CourseNotFoundException.class);
    }

    private CompanyInfo makeCompanyInfo() {
        Company company = Company.builder().id(1L).name("test company").build();
        Job job = Job.builder().id(1L).name("test job").build();
        DetailedPosition detailedPosition = DetailedPosition.builder().id(1L).name("test detailed position").build();
        return CompanyInfo.builder().id(1L).company(company).job(job).detailedPosition(detailedPosition).build();
    }

    private List<Course> makeCourseList(Job job) {
        List<Course> courseList = new ArrayList<>();
        courseList.add(Course.builder().id(1L).name("SKILL1").type(CourseType.SKILL).order(1).job(job).build());
        courseList.add(Course.builder().id(3L).name("SKILL2").type(CourseType.SKILL).order(3).job(job).build());
        courseList.add(Course.builder().id(2L).name("CS1").type(CourseType.CS).order(3).job(job).build());
        courseList.add(Course.builder().id(4L).name("CS2").type(CourseType.CS).order(4).job(job).build());
        courseList.add(Course.builder().id(5L).name("SKILL3").type(CourseType.SKILL).order(5).job(job).build());
        return courseList;
    }

    private List<CourseDetail> makeCourseDetailList(Course course) {
        List<CourseDetail> courseDetailList = new ArrayList<>();
        courseDetailList.add(CourseDetail.builder().id(1L).name("Java").course(course).build());
        courseDetailList.add(CourseDetail.builder().id(2L).name("Kotlin").course(course).build());
        return courseDetailList;
    }
}