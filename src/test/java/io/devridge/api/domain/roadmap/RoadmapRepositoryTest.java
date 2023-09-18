package io.devridge.api.domain.roadmap;

import io.devridge.api.config.JpaConfig;
import io.devridge.api.domain.companyinfo.*;
import io.devridge.api.domain.user.*;
import io.devridge.api.dto.course.RoadmapStatusDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@Import(JpaConfig.class)
@DataJpaTest
class RoadmapRepositoryTest {

    @Autowired
    private CompanyInfoRepository companyInfoRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private DetailedPositionRepository detailedPositionRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private RoadmapRepository roadmapRepository;

    @Autowired
    private UserRoadmapRepository userRoadmapRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager em;

    @DisplayName("로드맵을 가져올 때 코스 정보와 학습 상태를 가져오고 order 오름차순 정렬, 만약 같은 경우 SKILL이 먼저 오도록 한다.")
    @Test
    public void getRoadmapsIncludingCoursesByCompanyInfo_with_not_user_test() {
        Company company = companyRepository.save(Company.builder().name("test company").build());
        Job job = jobRepository.save(Job.builder().name("test job").build());
        DetailedPosition detailedPosition = detailedPositionRepository.save(DetailedPosition.builder().name("test detail").company(company).build());
        CompanyInfo companyInfo = companyInfoRepository.save(CompanyInfo.builder().job(job).detailedPosition(detailedPosition).company(company).build());
        Course course1 = courseRepository.save(Course.builder().job(job).name("SKILL1").type(CourseType.SKILL).order(1).build());
        Course course2 = courseRepository.save(Course.builder().job(job).name("CS1").type(CourseType.CS).order(3).build());
        Course course3 = courseRepository.save(Course.builder().job(job).name("CS2").type(CourseType.CS).order(2).build());
        Course course4 = courseRepository.save(Course.builder().job(job).name("SKILL2").type(CourseType.SKILL).order(3).build());
        roadmapRepository.save(Roadmap.builder().course(course1).companyInfo(companyInfo).matchingFlag(MatchingFlag.YES).build());
        roadmapRepository.save(Roadmap.builder().course(course2).companyInfo(companyInfo).matchingFlag(MatchingFlag.NO).build());
        roadmapRepository.save(Roadmap.builder().course(course3).companyInfo(companyInfo).matchingFlag(MatchingFlag.YES).build());
        roadmapRepository.save(Roadmap.builder().course(course4).companyInfo(companyInfo).matchingFlag(MatchingFlag.NO).build());
        em.flush();
        em.clear();

        // when
        List<RoadmapStatusDto> roadmapStatusDtoList = roadmapRepository.getRoadmapsIncludingCoursesByCompanyInfoIdWithUserId(companyInfo.getId(), null);

        // then
        assertThat(roadmapStatusDtoList.size()).isEqualTo(4);

        assertThat(roadmapStatusDtoList.get(0).getCourse().getId()).isEqualTo(course1.getId());
        assertThat(roadmapStatusDtoList.get(0).getMatchingFlag()).isEqualTo(MatchingFlag.YES);
        assertThat(roadmapStatusDtoList.get(0).getCourse().getName()).isEqualTo("SKILL1");
        assertThat(roadmapStatusDtoList.get(0).getCourse().getOrder()).isEqualTo(1);
        assertThat(roadmapStatusDtoList.get(0).getCourse().getType()).isEqualTo(CourseType.SKILL);
        assertThat(roadmapStatusDtoList.get(0).getStudyStatus()).isNull();

        assertThat(roadmapStatusDtoList.get(1).getCourse().getId()).isEqualTo(course3.getId());
        assertThat(roadmapStatusDtoList.get(1).getMatchingFlag()).isEqualTo(MatchingFlag.YES);
        assertThat(roadmapStatusDtoList.get(1).getCourse().getName()).isEqualTo("CS2");
        assertThat(roadmapStatusDtoList.get(1).getCourse().getOrder()).isEqualTo(2);
        assertThat(roadmapStatusDtoList.get(1).getCourse().getType()).isEqualTo(CourseType.CS);
        assertThat(roadmapStatusDtoList.get(1).getStudyStatus()).isNull();

        assertThat(roadmapStatusDtoList.get(2).getCourse().getId()).isEqualTo(course4.getId());
        assertThat(roadmapStatusDtoList.get(2).getMatchingFlag()).isEqualTo(MatchingFlag.NO);
        assertThat(roadmapStatusDtoList.get(2).getCourse().getName()).isEqualTo("SKILL2");
        assertThat(roadmapStatusDtoList.get(2).getCourse().getOrder()).isEqualTo(3);
        assertThat(roadmapStatusDtoList.get(2).getCourse().getType()).isEqualTo(CourseType.SKILL);
        assertThat(roadmapStatusDtoList.get(2).getStudyStatus()).isNull();

        assertThat(roadmapStatusDtoList.get(3).getCourse().getId()).isEqualTo(course2.getId());
        assertThat(roadmapStatusDtoList.get(3).getMatchingFlag()).isEqualTo(MatchingFlag.NO);
        assertThat(roadmapStatusDtoList.get(3).getCourse().getName()).isEqualTo("CS1");
        assertThat(roadmapStatusDtoList.get(3).getCourse().getOrder()).isEqualTo(3);
        assertThat(roadmapStatusDtoList.get(3).getCourse().getType()).isEqualTo(CourseType.CS);
        assertThat(roadmapStatusDtoList.get(3).getStudyStatus()).isNull();
    }


    @DisplayName("로드맵을 가져올 때 로그인한 유저의 학습상태도 같이 가져온다.")
    @Test
    public void getRoadmapsIncludingCoursesByCompanyInfo_with_user_test() {
        User user = userRepository.save(User.builder().name("test user").build());
        Company company = companyRepository.save(Company.builder().name("test company").build());
        Job job = jobRepository.save(Job.builder().name("test job").build());
        DetailedPosition detailedPosition = detailedPositionRepository.save(DetailedPosition.builder().name("test detail").company(company).build());
        CompanyInfo companyInfo = companyInfoRepository.save(CompanyInfo.builder().job(job).detailedPosition(detailedPosition).company(company).build());
        Course course1 = courseRepository.save(Course.builder().id(1L).job(job).name("SKILL1").type(CourseType.SKILL).order(1).build());
        Course course2 = courseRepository.save(Course.builder().id(2L).job(job).name("CS1").type(CourseType.CS).order(3).build());
        Course course3 = courseRepository.save(Course.builder().id(3L).job(job).name("CS2").type(CourseType.CS).order(2).build());
        Course course4 = courseRepository.save(Course.builder().id(4L).job(job).name("SKILL2").type(CourseType.SKILL).order(3).build());
        Roadmap roadmap1 = roadmapRepository.save(Roadmap.builder().course(course1).companyInfo(companyInfo).matchingFlag(MatchingFlag.YES).build());
        Roadmap roadmap2 = roadmapRepository.save(Roadmap.builder().course(course2).companyInfo(companyInfo).matchingFlag(MatchingFlag.NO).build());
        roadmapRepository.save(Roadmap.builder().course(course3).companyInfo(companyInfo).matchingFlag(MatchingFlag.YES).build());
        roadmapRepository.save(Roadmap.builder().course(course4).companyInfo(companyInfo).matchingFlag(MatchingFlag.NO).build());
        userRoadmapRepository.save(UserRoadmap.builder().studyStatus(StudyStatus.STUDYING).user(user).roadmap(roadmap1).build());
        userRoadmapRepository.save(UserRoadmap.builder().studyStatus(StudyStatus.STUDY_END).user(user).roadmap(roadmap2).build());
        em.flush();
        em.clear();

        // when
        List<RoadmapStatusDto> roadmapStatusDtoList = roadmapRepository.getRoadmapsIncludingCoursesByCompanyInfoIdWithUserId(companyInfo.getId(), 1L);

        // then
        assertThat(roadmapStatusDtoList.size()).isEqualTo(4);

        assertThat(roadmapStatusDtoList.get(0).getCourse().getName()).isEqualTo("SKILL1");
        assertThat(roadmapStatusDtoList.get(0).getCourse().getType()).isEqualTo(CourseType.SKILL);
        assertThat(roadmapStatusDtoList.get(0).getStudyStatus()).isEqualTo(StudyStatus.STUDYING);

        assertThat(roadmapStatusDtoList.get(1).getCourse().getName()).isEqualTo("CS2");
        assertThat(roadmapStatusDtoList.get(1).getCourse().getType()).isEqualTo(CourseType.CS);
        assertThat(roadmapStatusDtoList.get(1).getStudyStatus()).isNull();

        assertThat(roadmapStatusDtoList.get(2).getCourse().getName()).isEqualTo("SKILL2");
        assertThat(roadmapStatusDtoList.get(2).getCourse().getType()).isEqualTo(CourseType.SKILL);
        assertThat(roadmapStatusDtoList.get(2).getStudyStatus()).isNull();

        assertThat(roadmapStatusDtoList.get(3).getCourse().getName()).isEqualTo("CS1");
        assertThat(roadmapStatusDtoList.get(3).getCourse().getType()).isEqualTo(CourseType.CS);
        assertThat(roadmapStatusDtoList.get(3).getStudyStatus()).isEqualTo(StudyStatus.STUDY_END);
    }
}