package io.devridge.api.service;

import io.devridge.api.domain.companyinfo.Company;
import io.devridge.api.domain.companyinfo.CompanyJobRepository;
import io.devridge.api.domain.companyinfo.Job;
import io.devridge.api.domain.roadmap.*;
import io.devridge.api.domain.video.CourseVideoRepository;
import io.devridge.api.dto.course.CompanyJobInfo;
import io.devridge.api.dto.course.CourseListResponseDto;
import io.devridge.api.handler.ex.CompanyJobNotFoundException;
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
    private CourseVideoRepository courseVideoRepository;

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


    private List<Course> makeCourseList(Job job) {
        List<Course> courseList = new ArrayList<>();
        courseList.add(Course.builder().id(1L).name("SKILL1").type(CourseType.SKILL).order(1).job(job).build());
        courseList.add(Course.builder().id(3L).name("SKILL2").type(CourseType.SKILL).order(3).job(job).build());
        courseList.add(Course.builder().id(2L).name("CS1").type(CourseType.CS).order(3).job(job).build());
        courseList.add(Course.builder().id(4L).name("CS2").type(CourseType.CS).order(4).job(job).build());
        courseList.add(Course.builder().id(5L).name("SKILL3").type(CourseType.SKILL).order(5).job(job).build());
        return courseList;
    }
}