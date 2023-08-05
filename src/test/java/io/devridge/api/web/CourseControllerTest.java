package io.devridge.api.web;

import io.devridge.api.domain.company_job.*;
import io.devridge.api.domain.course.Course;
import io.devridge.api.domain.course.CourseRepository;
import io.devridge.api.domain.course.CourseType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class CourseControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private CompanyJobRepository companyJobRepository;
    
    @BeforeEach
    public void setUp() {
        Company company = companyRepository.save(Company.builder().name("토스 증권").build());
        Job job = jobRepository.save(Job.builder().name("백엔드").build());
        companyJobRepository.save(CompanyJob.builder().company(company).job(job).build());
        courseRepository.save(Course.builder().name("SKILL1").type(CourseType.SKILL).turn(1).job(job).build());
        courseRepository.save(Course.builder().name("SKILL2").type(CourseType.SKILL).turn(3).job(job).build());
        courseRepository.save(Course.builder().name("CS1").type(CourseType.CS).turn(3).job(job).build());
        courseRepository.save(Course.builder().name("CS2").type(CourseType.CS).turn(4).job(job).build());
    }

    @DisplayName("코스 조회 성공")
    @Test
    public void courses_success_test() throws Exception {
        // given
        long companyId = 1L;
        long jobId = 1L;

        // when
        ResultActions resultActions = mvc.perform(
                get("/courses")
                        .param("company", Long.toString(companyId))
                        .param("job", Long.toString(jobId))
        );

        // then
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.status").value("success"));
        resultActions.andExpect(jsonPath("$.data.courseList").isNotEmpty());
    }

    @DisplayName("코스 조회 실패 - 회사, 직무 정보 오류")
    @ParameterizedTest
    @CsvSource({
            "1, 100",
            "100, 1",
    })
    public void courses_fail_test(long companyId, long jobId) throws Exception {
        // given & when
        ResultActions resultActions = mvc.perform(
                get("/courses")
                        .param("company", Long.toString(companyId))
                        .param("job", Long.toString(jobId))
        );

        // then
        resultActions.andExpect(status().isBadRequest());
        resultActions.andExpect(jsonPath("$.status").value("error"));
        resultActions.andExpect(jsonPath("$.message").value("회사와 직무에 일치 하는 정보가 없습니다."));
    }
}