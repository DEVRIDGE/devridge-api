package io.devridge.api.service;

import io.devridge.api.domain.companyinfo.*;
import io.devridge.api.domain.roadmap.*;
import io.devridge.api.domain.video.CourseVideoRepository;
import io.devridge.api.dto.CourseDetailResponseDto;
import io.devridge.api.dto.course.CompanyJobInfo;
import io.devridge.api.dto.course.CourseListResponseDto;
import io.devridge.api.handler.ex.CompanyInfoNotFoundException;
import io.devridge.api.handler.ex.CompanyJobNotFoundException;
import io.devridge.api.handler.ex.CourseNotFoundException;
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
    private CourseDetailRepository courseDetailRepository;

    @Mock
    private CompanyInfoRepository companyInfoRepository;

    @DisplayName("코스리스트를 turn으로 모아서 리스트를 만들고 만약 SKILL과 SKILL 사이에 아무 것도 없다면 빈 리스트가 추가된다")
    @Test
    public void getCourseList_success_test() {
        // given
        Company company = Company.builder().id(1L).name("test company").build();
        Job job = Job.builder().id(1L).name("test job").build();
        CompanyJobInfo companyJobInfo = CompanyJobInfo.builder().companyName(company.getName()).jobName(job.getName()).build();

        List<Course> courseList = makeCourseList(job);

        long companyId = 1L;
        long jobId = 1L;

        // stub
        when(companyJobRepository.findCompanyJobInfo(companyId, jobId))
                .thenReturn(Optional.of(companyJobInfo));

        when(courseRepository.getCourseListByJob(jobId))
                .thenReturn(courseList);

        // when
        CourseListResponseDto courseListResponseDto = courseService.getCourseList(companyId, jobId);

        // then
        assertThat(courseListResponseDto.getCourseList().size()).isEqualTo(5);
        assertThat(courseListResponseDto.getCompanyName()).isEqualTo("test company");
        assertThat(courseListResponseDto.getJobName()).isEqualTo("test job");
        assertThat(courseListResponseDto.getCourseList().get(0).getIndex()).isEqualTo(0);
        assertThat(courseListResponseDto.getCourseList().get(1).getIndex()).isEqualTo(1);
        assertThat(courseListResponseDto.getCourseList().get(1).getCourses().size()).isEqualTo(0);
        assertThat(courseListResponseDto.getCourseList().get(2).getCourses().get(0).getName()).isEqualTo("SKILL2");
        assertThat(courseListResponseDto.getCourseList().get(2).getCourses().get(1).getName()).isEqualTo("CS1");
        assertThat(courseListResponseDto.getCourseList().get(3).getCourses().get(0).getName()).isEqualTo("CS2");
        assertThat(courseListResponseDto.getCourseList().get(4).getCourses().get(0).getName()).isEqualTo("SKILL3");
    }

    @DisplayName("회사와 직무가 일치하지 않으면 에러를 던진다")
    @Test
    public void getCourseList_fail_test() {
        // given
        long companyId = 1L;
        long jobId = 2L;

        // stub
        when(companyJobRepository.findCompanyJobInfo(companyId, jobId))
                .thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> courseService.getCourseList(companyId, jobId))
                .isInstanceOf(CompanyJobNotFoundException.class);
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