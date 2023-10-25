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

//    @Autowired
//    private CompanyInfoRepository companyInfoRepository;
//    @Autowired
//    private CourseRepository courseRepository;
//    @Autowired
//    private CourseDetailRepository courseDetailRepository;
//    @Autowired
//    private CompanyRequiredAbilityRepository companyRequiredAbilityRepository;
//    @Autowired
//    private EntityManager em;
//
//    @DisplayName("코스 상세와 회사 요구 역량이 매칭되는 코스 상세 id 리스트를 반환한다.")
//    @Test
//    void get_matching_course_detail_ids_for_company_ability() {
//        // given
//        Course course1 = courseRepository.save(Course.builder().build());
//        Course course2 = courseRepository.save(Course.builder().build());
//        CourseDetail courseDetail1 = courseDetailRepository.save(CourseDetail.builder().course(course1).build());
//        courseDetailRepository.save(CourseDetail.builder().course(course1).build());
//        CourseDetail courseDetail3 = courseDetailRepository.save(CourseDetail.builder().course(course2).build());
//        courseDetailRepository.save(CourseDetail.builder().course(course2).build());
//        CompanyInfo companyInfo = companyInfoRepository.save(CompanyInfo.builder().id(1L).build());
//        companyRequiredAbilityRepository.save(CompanyRequiredAbility.builder().companyInfo(companyInfo).courseDetail(courseDetail1).build());
//        companyRequiredAbilityRepository.save(CompanyRequiredAbility.builder().companyInfo(companyInfo).courseDetail(courseDetail3).build());
//        em.flush();
//        em.clear();
//
//        // when
//        List<Long> resultList = courseDetailRepository.getMatchingCourseDetailIdsForCompanyAbility(companyInfo.getId());
//
//        // then
//        assertThat(resultList.size()).isEqualTo(2);
//        assertThat(resultList.get(0)).isEqualTo(courseDetail1.getId());
//        assertThat(resultList.get(1)).isEqualTo(courseDetail3.getId());
//    }
//
//    @DisplayName("코스 상세 목록에서 회사 요구 역량과 매칭되는 값에는 회사 요구 역량 값이 있고 아닌 경우는 없다. 정렬의 경우 이름 오름차순으로 정렬된다.")
//    @Test
//    void get_course_detail_list_with_ability_by_course_id_order_by_name() {
//        // given
//        Course course1 = courseRepository.save(Course.builder().build());
//        Course course2 = courseRepository.save(Course.builder().build());
//        CourseDetail courseDetail1 = courseDetailRepository.save(CourseDetail.builder().name("Java").course(course1).build());
//        CourseDetail courseDetail2 = courseDetailRepository.save(CourseDetail.builder().course(course1).name("C++").build());
//        CourseDetail courseDetail3 = courseDetailRepository.save(CourseDetail.builder().name("Mysql").course(course2).build());
//        courseDetailRepository.save(CourseDetail.builder().course(course2).name("MongoDB").build());
//        CompanyInfo companyInfo = companyInfoRepository.save(CompanyInfo.builder().build());
//        CompanyRequiredAbility companyRequiredAbility = companyRequiredAbilityRepository.save(CompanyRequiredAbility.builder().companyInfo(companyInfo).courseDetail(courseDetail1).build());
//        companyRequiredAbilityRepository.save(CompanyRequiredAbility.builder().companyInfo(companyInfo).courseDetail(courseDetail3).build());
//        em.flush();
//        em.clear();
//        List<Long> filteredCourseDetailIds = List.of(courseDetail1.getId(), courseDetail3.getId());
//
//        // when
//        List<CourseDetailWithAbilityDto> resultList = courseDetailRepository.getCourseDetailListWithAbilityByCourseIdOrderByName(course1.getId(), companyInfo.getId(), filteredCourseDetailIds);
//
//        // then
//        assertThat(resultList.size()).isEqualTo(2);
//        assertThat(resultList.get(0).getCourseDetailId()).isEqualTo(courseDetail2.getId());
//        assertThat(resultList.get(0).getCourseDetailName()).isEqualTo("C++");
//        assertThat(resultList.get(0).getCompanyRequiredAbilityId()).isNull();
//        assertThat(resultList.get(1).getCourseDetailId()).isEqualTo(courseDetail1.getId());
//        assertThat(resultList.get(1).getCourseDetailName()).isEqualTo("Java");
//        assertThat(resultList.get(1).getCompanyRequiredAbilityId()).isEqualTo(companyRequiredAbility.getId());
//    }
}