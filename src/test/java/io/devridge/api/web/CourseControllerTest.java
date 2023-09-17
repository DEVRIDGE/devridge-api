package io.devridge.api.web;

import io.devridge.api.domain.companyinfo.*;
import io.devridge.api.domain.roadmap.*;
import io.devridge.api.dto.course.RoadmapStatusDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class CourseControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private RoadmapRepository roadmapRepository;

    @MockBean
    private CompanyInfoRepository companyInfoRepository;

    // 로그인
    @DisplayName("/courses 요청시 코스 리스트를 응답한다.")
    @Test
    public void course_list_success_test() throws Exception {
        // given
        Company company = Company.builder().id(1L).name("test company").build();
        Job job = Job.builder().id(1L).name("test job").build();
        DetailedPosition detailedPosition = DetailedPosition.builder().id(1L).name("test detailed position").build();
        CompanyInfo companyInfo = CompanyInfo.builder().id(1L).company(company).job(job).detailedPosition(detailedPosition).build();

        Course course1 = Course.builder().id(1L).name("SKILL1").type(CourseType.SKILL).order(1).job(job).build();
        Course course2 = Course.builder().id(2L).name("SKILL2").type(CourseType.SKILL).order(3).job(job).build();
        Course course3 = Course.builder().id(3L).name("CS1").type(CourseType.CS).order(3).job(job).build();
        Course course4 = Course.builder().id(4L).name("CS2").type(CourseType.CS).order(4).job(job).build();

        List<RoadmapStatusDto> roadmapStatusDtoList = new ArrayList<>();
        roadmapStatusDtoList.add(RoadmapStatusDto.builder().matchingFlag(MatchingFlag.YES).course(course1).studyStatus(null).build());
        roadmapStatusDtoList.add(RoadmapStatusDto.builder().matchingFlag(MatchingFlag.YES).course(course2).studyStatus(null).build());
        roadmapStatusDtoList.add(RoadmapStatusDto.builder().matchingFlag(MatchingFlag.NO).course(course3).studyStatus(null).build());
        roadmapStatusDtoList.add(RoadmapStatusDto.builder().matchingFlag(MatchingFlag.NO).course(course4).studyStatus(null).build());

        // stub
        when(companyInfoRepository.findByCompanyIdAndJobIdAndDetailedPositionIdWithFetchJoin(company.getId(), job.getId(), detailedPosition.getId())).thenReturn(Optional.of(companyInfo));
        when(roadmapRepository.getRoadmapsIncludingCoursesByCompanyInfoIdWithUserId(anyLong(), isNull())).thenReturn(roadmapStatusDtoList);

        // when
        ResultActions resultActions = mvc.perform(
                get("/courses")
                        .param("company", Long.toString(1L))
                        .param("job", Long.toString(1L))
                        .param("detailedPosition", Long.toString(1L))
        );

        // then
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.status").value("success"));
        resultActions.andExpect(jsonPath("$.message").value("요청 성공"));
        resultActions.andExpect(jsonPath("$.data.companyName").value("test company"));
        resultActions.andExpect(jsonPath("$.data.jobName").value("test job"));
        resultActions.andExpect(jsonPath("$.data.courseList", hasSize(5)));
        
        // 첫번째 skill이 나오면 빈 리스트
        resultActions.andExpect(jsonPath("$.data.courseList[0].index").value(0));
        resultActions.andExpect(jsonPath("$.data.courseList[0].courses", hasSize(0)));

        resultActions.andExpect(jsonPath("$.data.courseList[1].index").value(1));
        resultActions.andExpect(jsonPath("$.data.courseList[1].courses[0].id").value(1));
        resultActions.andExpect(jsonPath("$.data.courseList[1].courses[0].name").value("SKILL1"));
        resultActions.andExpect(jsonPath("$.data.courseList[1].courses[0].type").value("SKILL"));
        resultActions.andExpect(jsonPath("$.data.courseList[1].courses[0].matchingFlag").value("YES"));
        
        // skill과 skill 사이는 빈 리스트
        resultActions.andExpect(jsonPath("$.data.courseList[2].index").value(2));
        resultActions.andExpect(jsonPath("$.data.courseList[2].courses", hasSize(0)));

        // order가 같으면 같은 리스트에 들어간다.
        resultActions.andExpect(jsonPath("$.data.courseList[3].index").value(3));
        resultActions.andExpect(jsonPath("$.data.courseList[3].courses[0].id").value(2));
        resultActions.andExpect(jsonPath("$.data.courseList[3].courses[0].name").value("SKILL2"));
        resultActions.andExpect(jsonPath("$.data.courseList[3].courses[0].type").value("SKILL"));
        resultActions.andExpect(jsonPath("$.data.courseList[3].courses[0].matchingFlag").value("YES"));
        resultActions.andExpect(jsonPath("$.data.courseList[3].courses[1].id").value(3));
        resultActions.andExpect(jsonPath("$.data.courseList[3].courses[1].name").value("CS1"));
        resultActions.andExpect(jsonPath("$.data.courseList[3].courses[1].type").value("CS"));
        resultActions.andExpect(jsonPath("$.data.courseList[3].courses[1].matchingFlag").value("NO"));

        resultActions.andExpect(jsonPath("$.data.courseList[4].index").value(4));
        resultActions.andExpect(jsonPath("$.data.courseList[4].courses[0].id").value(4));
        resultActions.andExpect(jsonPath("$.data.courseList[4].courses[0].name").value("CS2"));
        resultActions.andExpect(jsonPath("$.data.courseList[4].courses[0].type").value("CS"));
        resultActions.andExpect(jsonPath("$.data.courseList[4].courses[0].matchingFlag").value("NO"));
    }

    @DisplayName("/courses 요청시 잘못된 요청을 하면 에러를 응답한다.")
    @ParameterizedTest
    @MethodSource("invalidACourseListArgument")
    public void request_courses_list_with_wrong_argument_response_error(String companyId, String jobId, String detailedId) throws Exception {
        // given & when
        ResultActions resultActions = mvc.perform(
                get("/courses")
                        .param("company", companyId)
                        .param("job", jobId)
                        .param("detailedPosition", detailedId)
        );

        // then
        resultActions.andExpect(status().isBadRequest());
        resultActions.andExpect(jsonPath("$.status").value("error"));
        resultActions.andExpect(jsonPath("$.message").value("잘못된 형식 요청"));
        resultActions.andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("/courses 요청시 회사 정보가 존재하지 않으면 에러를 응답한다.")
    @Test
    public void request_courses_list_with_not_have_company_info_response_error() throws Exception {
        // stub
        when(companyInfoRepository.findByCompanyIdAndJobIdAndDetailedPositionIdWithFetchJoin(anyLong(), anyLong(), anyLong())).thenReturn(Optional.empty());

        // when
        ResultActions resultActions = mvc.perform(
                get("/courses")
                        .param("company", "1")
                        .param("job", "1")
                        .param("detailedPosition", "1")
        );
        
        // then
        resultActions.andExpect(status().isBadRequest());
        resultActions.andExpect(jsonPath("$.status").value("error"));
        resultActions.andExpect(jsonPath("$.message").value("회사, 직무, 서비스에 일치 하는 회사 정보가 없습니다."));
        resultActions.andExpect(jsonPath("$.data").isEmpty());
    }

    static Stream<Arguments> invalidACourseListArgument() {
        return Stream.of(
                Arguments.of("", "1", "1"),
                Arguments.of(" ", "1", "1"),
                Arguments.of("wrong", "1", "1"),
                Arguments.of("1", "", "1"),
                Arguments.of("1", " ", "1"),
                Arguments.of("1", "wrong", "1"),
                Arguments.of("1", "1", ""),
                Arguments.of("1", "1", " "),
                Arguments.of("1", "1", "wrong")
        );
    }
}