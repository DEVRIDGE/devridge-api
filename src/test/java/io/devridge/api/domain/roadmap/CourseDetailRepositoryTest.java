package io.devridge.api.domain.roadmap;

import io.devridge.api.config.JpaConfig;
import io.devridge.api.domain.companyinfo.*;
import io.devridge.api.dto.course.CourseDetailWithAbilityDto;
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
    private CourseToDetailRepository courseToDetailRepository;
    @Autowired
    private CompanyInfoCompanyRequiredAbilityRepository companyInfoCompanyRequiredAbilityRepository;
    @Autowired
    private CourseToDetailRepository courseDetailToRepository;
    @Autowired
    private EntityManager em;

    @DisplayName("코스 상세와 회사 요구 역량이 매칭되는 코스 상세 id 리스트를 반환한다.")
    @Test
    void get_matching_course_detail_ids_for_company_ability() {
        // given
        Course course1 = courseRepository.save(Course.builder().build());
        Course course2 = courseRepository.save(Course.builder().build());
        CourseDetail courseDetail1 = courseDetailRepository.save(CourseDetail.builder().id(1L).build());
        CourseDetail courseDetail2 = courseDetailRepository.save(CourseDetail.builder().id(2L).build());
        CourseDetail courseDetail3 = courseDetailRepository.save(CourseDetail.builder().id(3L).build());
        CourseDetail courseDetail4 = courseDetailRepository.save(CourseDetail.builder().id(4L).build());
        courseToDetailRepository.save(CourseToDetail.builder().course(course1).courseDetail(courseDetail1).build());
        courseToDetailRepository.save(CourseToDetail.builder().course(course1).courseDetail(courseDetail2).build());
        courseToDetailRepository.save(CourseToDetail.builder().course(course2).courseDetail(courseDetail3).build());
        courseToDetailRepository.save(CourseToDetail.builder().course(course2).courseDetail(courseDetail4).build());

        CompanyInfo companyInfo = companyInfoRepository.save(CompanyInfo.builder().id(1L).build());
        CompanyRequiredAbility companyRequiredAbility1 = companyRequiredAbilityRepository.save(CompanyRequiredAbility.builder().courseDetail(courseDetail1).build());
        CompanyRequiredAbility companyRequiredAbility2 = companyRequiredAbilityRepository.save(CompanyRequiredAbility.builder().courseDetail(courseDetail3).build());

        companyInfoCompanyRequiredAbilityRepository.save(CompanyInfoCompanyRequiredAbility.builder().companyInfo(companyInfo).companyRequiredAbility(companyRequiredAbility1).build());
        companyInfoCompanyRequiredAbilityRepository.save(CompanyInfoCompanyRequiredAbility.builder().companyInfo(companyInfo).companyRequiredAbility(companyRequiredAbility2).build());

        em.flush();
        em.clear();

        // when
        List<Long> resultList = courseDetailRepository.getMatchingCourseDetailIdsForCompanyAbility(companyInfo.getId());

        // then
        assertThat(resultList.size()).isEqualTo(2);
        assertThat(resultList.get(0)).isEqualTo(1L);
        assertThat(resultList.get(1)).isEqualTo(3L);
    }

    @DisplayName("코스 상세 목록에서 코스 상세 목록에서 회사 요구 역량과 매칭되는 값에는 회사 요구 역량 값이 있고 아닌 경우는 없다. 정렬의 경우 이름 오름차순으로 정렬된다.")
    @Test
    void get_course_detail_list_with_ability_by_course_id_order_by_name() {
        // given
        Course course1 = courseRepository.save(Course.builder().build());
        Course course2 = courseRepository.save(Course.builder().build());
        CourseDetail courseDetail1 = courseDetailRepository.save(CourseDetail.builder().name("Java").build());
        CourseDetail courseDetail2 = courseDetailRepository.save(CourseDetail.builder().name("C++").build());
        CourseDetail courseDetail3 = courseDetailRepository.save(CourseDetail.builder().name("Mysql").build());
        CourseDetail courseDetail4 = courseDetailRepository.save(CourseDetail.builder().name("MongoDB").build());
        courseDetailToRepository.save(CourseToDetail.builder().course(course1).courseDetail(courseDetail1).build());
        courseDetailToRepository.save(CourseToDetail.builder().course(course1).courseDetail(courseDetail2).build());
        courseDetailToRepository.save(CourseToDetail.builder().course(course2).courseDetail(courseDetail3).build());
        courseDetailToRepository.save(CourseToDetail.builder().course(course2).courseDetail(courseDetail4).build());

        CompanyInfo companyInfo = companyInfoRepository.save(CompanyInfo.builder().build());
        CompanyRequiredAbility companyRequiredAbility1 = companyRequiredAbilityRepository.save(CompanyRequiredAbility.builder().id(1L).courseDetail(courseDetail1).build());
        CompanyRequiredAbility companyRequiredAbility2 = companyRequiredAbilityRepository.save(CompanyRequiredAbility.builder().id(2L).courseDetail(courseDetail3).build());
        companyInfoCompanyRequiredAbilityRepository.save(CompanyInfoCompanyRequiredAbility.builder().companyInfo(companyInfo).companyRequiredAbility(companyRequiredAbility1).build());
        companyInfoCompanyRequiredAbilityRepository.save(CompanyInfoCompanyRequiredAbility.builder().companyInfo(companyInfo).companyRequiredAbility(companyRequiredAbility2).build());
        em.flush();
        em.clear();

        List<Long> filteredCourseDetailIds = List.of(courseDetail1.getId(), courseDetail3.getId());

        // when
        List<CourseDetailWithAbilityDto> resultList = courseDetailRepository.getCourseDetailListWithAbilityByCourseIdOrderByName(course1.getId(), companyInfo.getId(), filteredCourseDetailIds);

        // then
        assertThat(resultList.size()).isEqualTo(2);
        assertThat(resultList.get(0).getCourseDetailId()).isEqualTo(courseDetail2.getId());
        assertThat(resultList.get(0).getCourseDetailName()).isEqualTo("C++");
        assertThat(resultList.get(0).getCompanyRequiredAbilityId()).isNull();
        assertThat(resultList.get(1).getCourseDetailId()).isEqualTo(courseDetail1.getId());
        assertThat(resultList.get(1).getCourseDetailName()).isEqualTo("Java");
        assertThat(resultList.get(1).getCompanyRequiredAbilityId()).isEqualTo(companyRequiredAbility1.getId());
    }
}