package io.devridge.api.domain.roadmap;

import io.devridge.api.config.JpaConfig;
import io.devridge.api.domain.companyinfo.*;
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
    private EntityManager em;

    @DisplayName("로드맵을 가져올 떄 코스 정보를 같이 가져오고 order 오름차순으로 정렬한다. 만약 같은 경우 SKILL이 먼저 오도록 한다.")
    @Test
    public void findByCompanyIdAndJobIdAndDetailedPositionIdWithFetchJoin_test() {
        // given
        Company company = companyRepository.save(Company.builder().name("test company").build());
        Job job = jobRepository.save(Job.builder().name("test job").build());
        DetailedPosition detailedPosition = detailedPositionRepository.save(DetailedPosition.builder().name("test detail").company(company).build());
        CompanyInfo companyInfo = companyInfoRepository.save(CompanyInfo.builder().job(job).detailedPosition(detailedPosition).company(company).build());
        Course course1 = courseRepository.save(Course.builder().job(job).name("SKILL1").type(CourseType.SKILL).order(1).build());
        Course course2 = courseRepository.save(Course.builder().job(job).name("CS1").type(CourseType.CS).order(3).build());
        Course course3 = courseRepository.save(Course.builder().job(job).name("CS2").type(CourseType.CS).order(2).build());
        Course course4 = courseRepository.save(Course.builder().job(job).name("SKILL2").type(CourseType.SKILL).order(3).build());
        roadmapRepository.save(Roadmap.builder().course(course1).companyInfo(companyInfo).matchingFlag(MatchingStatus.YES).build());
        roadmapRepository.save(Roadmap.builder().course(course2).companyInfo(companyInfo).matchingFlag(MatchingStatus.NO).build());
        roadmapRepository.save(Roadmap.builder().course(course3).companyInfo(companyInfo).matchingFlag(MatchingStatus.YES).build());
        roadmapRepository.save(Roadmap.builder().course(course4).companyInfo(companyInfo).matchingFlag(MatchingStatus.NO).build());
        em.flush();
        em.clear();

        // when
        List<Roadmap> roadmapList = roadmapRepository.getRoadmapsIncludingCoursesByCompanyInfoId(companyInfo.getId());

        // then
        assertThat(roadmapList.size()).isEqualTo(4);

        assertThat(roadmapList.get(0).getMatchingFlag()).isEqualTo(MatchingStatus.YES);
        assertThat(roadmapList.get(0).getCourse().getId()).isEqualTo(1L);
        assertThat(roadmapList.get(0).getCourse().getName()).isEqualTo("SKILL1");
        assertThat(roadmapList.get(0).getCourse().getOrder()).isEqualTo(1);
        assertThat(roadmapList.get(0).getCourse().getType()).isEqualTo(CourseType.SKILL);

        assertThat(roadmapList.get(1).getCourse().getId()).isEqualTo(3L);
        assertThat(roadmapList.get(1).getMatchingFlag()).isEqualTo(MatchingStatus.YES);
        assertThat(roadmapList.get(1).getCourse().getName()).isEqualTo("CS2");
        assertThat(roadmapList.get(1).getCourse().getOrder()).isEqualTo(2);
        assertThat(roadmapList.get(1).getCourse().getType()).isEqualTo(CourseType.CS);

        assertThat(roadmapList.get(2).getCourse().getId()).isEqualTo(4L);
        assertThat(roadmapList.get(2).getMatchingFlag()).isEqualTo(MatchingStatus.NO);
        assertThat(roadmapList.get(2).getCourse().getName()).isEqualTo("SKILL2");
        assertThat(roadmapList.get(2).getCourse().getOrder()).isEqualTo(3);
        assertThat(roadmapList.get(2).getCourse().getType()).isEqualTo(CourseType.SKILL);

        assertThat(roadmapList.get(3).getCourse().getId()).isEqualTo(2L);
        assertThat(roadmapList.get(3).getMatchingFlag()).isEqualTo(MatchingStatus.NO);
        assertThat(roadmapList.get(3).getCourse().getName()).isEqualTo("CS1");
        assertThat(roadmapList.get(3).getCourse().getOrder()).isEqualTo(3);
        assertThat(roadmapList.get(3).getCourse().getType()).isEqualTo(CourseType.CS);
    }
}