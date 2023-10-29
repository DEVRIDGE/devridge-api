package io.devridge.api.domain.roadmap;

import io.devridge.api.config.JpaConfig;
import io.devridge.api.domain.companyinfo.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@Import(JpaConfig.class)
@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class CourseDetailRepositoryTest {
    @Autowired
    private CompanyInfoRepository companyInfoRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private CourseDetailRepository courseDetailRepository;
    @Autowired
    private CompanyRequiredAbilityRepository companyRequiredAbilityRepository;
    @Autowired
    private CompanyInfoCompanyRequiredAbilityRepository companyInfoCompanyRequiredAbilityRepository;
    @Autowired
    private CourseToDetailRepository courseDetailToRepository;
    @Autowired
    private EntityManager em;

    @DisplayName("회사 요구 역량과 매칭되는 코스 상세 목록을 불러오는데 이름으로 오름차순 된다.")
    @Test
    void get_course_detail_list_with_ability_by_course_id_order_by_name() {
        // given
        Course course1 = courseRepository.save(Course.builder().build());
        Course course2 = courseRepository.save(Course.builder().build());
        CourseDetail courseDetail1 = courseDetailRepository.save(CourseDetail.builder().name("Java").build());
        CourseDetail courseDetail2 = courseDetailRepository.save(CourseDetail.builder().name("C++").build());
        CourseDetail courseDetail3 = courseDetailRepository.save(CourseDetail.builder().name("Mysql").build());
        CourseDetail courseDetail4 = courseDetailRepository.save(CourseDetail.builder().name("MongoDB").build());
        CourseDetail courseDetail5 = courseDetailRepository.save(CourseDetail.builder().name("Javascript").build());
        courseDetailToRepository.save(CourseToDetail.builder().course(course1).courseDetail(courseDetail1).build());
        courseDetailToRepository.save(CourseToDetail.builder().course(course1).courseDetail(courseDetail2).build());
        courseDetailToRepository.save(CourseToDetail.builder().course(course2).courseDetail(courseDetail3).build());
        courseDetailToRepository.save(CourseToDetail.builder().course(course2).courseDetail(courseDetail4).build());
        courseDetailToRepository.save(CourseToDetail.builder().course(course1).courseDetail(courseDetail5).build());

        CompanyInfo companyInfo = companyInfoRepository.save(CompanyInfo.builder().build());
        CompanyRequiredAbility companyRequiredAbility1 = companyRequiredAbilityRepository.save(CompanyRequiredAbility.builder().id(1L).courseDetail(courseDetail1).build());
        CompanyRequiredAbility companyRequiredAbility2 = companyRequiredAbilityRepository.save(CompanyRequiredAbility.builder().id(2L).courseDetail(courseDetail3).build());
        CompanyRequiredAbility companyRequiredAbility3 = companyRequiredAbilityRepository.save(CompanyRequiredAbility.builder().id(3L).courseDetail(courseDetail5).build());
        companyInfoCompanyRequiredAbilityRepository.save(CompanyInfoCompanyRequiredAbility.builder().companyInfo(companyInfo).companyRequiredAbility(companyRequiredAbility1).build());
        companyInfoCompanyRequiredAbilityRepository.save(CompanyInfoCompanyRequiredAbility.builder().companyInfo(companyInfo).companyRequiredAbility(companyRequiredAbility2).build());
        companyInfoCompanyRequiredAbilityRepository.save(CompanyInfoCompanyRequiredAbility.builder().companyInfo(companyInfo).companyRequiredAbility(companyRequiredAbility3).build());
        em.flush();
        em.clear();

        // when
        List<CourseDetail> resultList = courseDetailRepository.getCourseDetailListWithAbilityByCourseIdOrderByName(course1.getId(), companyInfo.getId());

        // then
        assertThat(resultList.size()).isEqualTo(2);
        assertThat(resultList.get(0).getId()).isEqualTo(courseDetail1.getId());
        assertThat(resultList.get(0).getName()).isEqualTo("Java");
        assertThat(resultList.get(1).getId()).isEqualTo(courseDetail5.getId());
        assertThat(resultList.get(1).getName()).isEqualTo("Javascript");
    }

    @DisplayName("코스 id와 매칭되는 모든 코스 상세 리스트를 가져온다.")
    @Test
    void get_course_detail_list_by_course_id_order_by_name() {
        // given
        Course course1 = courseRepository.save(Course.builder().build());
        Course course2 = courseRepository.save(Course.builder().build());
        CourseDetail courseDetail1 = courseDetailRepository.save(CourseDetail.builder().name("Java").build());
        CourseDetail courseDetail2 = courseDetailRepository.save(CourseDetail.builder().name("C++").build());
        CourseDetail courseDetail3 = courseDetailRepository.save(CourseDetail.builder().name("Mysql").build());
        CourseDetail courseDetail4 = courseDetailRepository.save(CourseDetail.builder().name("MongoDB").build());
        CourseDetail courseDetail5 = courseDetailRepository.save(CourseDetail.builder().name("Javascript").build());
        courseDetailToRepository.save(CourseToDetail.builder().course(course1).courseDetail(courseDetail1).build());
        courseDetailToRepository.save(CourseToDetail.builder().course(course1).courseDetail(courseDetail2).build());
        courseDetailToRepository.save(CourseToDetail.builder().course(course2).courseDetail(courseDetail3).build());
        courseDetailToRepository.save(CourseToDetail.builder().course(course2).courseDetail(courseDetail4).build());
        courseDetailToRepository.save(CourseToDetail.builder().course(course1).courseDetail(courseDetail5).build());

        em.flush();
        em.clear();

        // when
        List<CourseDetail> resultList = courseDetailRepository.getAllCourseDetailByCourseIdOrderByName(course1.getId());

        // then
        assertThat(resultList.size()).isEqualTo(3);
        assertThat(resultList.get(0).getId()).isEqualTo(courseDetail2.getId());
        assertThat(resultList.get(0).getName()).isEqualTo("C++");
        assertThat(resultList.get(1).getId()).isEqualTo(courseDetail1.getId());
        assertThat(resultList.get(1).getName()).isEqualTo("Java");
        assertThat(resultList.get(2).getId()).isEqualTo(courseDetail5.getId());
        assertThat(resultList.get(2).getName()).isEqualTo("Javascript");
    }
}