package io.devridge.api.domain.roadmap;

import io.devridge.api.domain.companyinfo.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.List;


@Transactional
@SpringBootTest
class CourseDetailRepositoryTest {
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private DetailedPositionRepository detailedPositionRepository;
    @Autowired
    private CompanyInfoRepository companyInfoRepository;

    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private CourseDetailRepository courseDetailRepository;
    @Autowired
    private CompanyRequiredAbilityRepository companyRequiredAbilityRepository;

    @Test
    @DisplayName("코스, 회사, 직무, 서비스 정보가 주어지면 해당하는 CourseDetail 리스트를 반환한다")
    void getCourseDetailList_exist_test() {
        //given
        Company company = companyRepository.save(new Company("토스"));
        Job job = jobRepository.save(new Job("백엔드"));
        DetailedPosition detailedPosition = detailedPositionRepository.save(new DetailedPosition("Product", company));
        CompanyInfo companyInfo = companyInfoRepository.save(new CompanyInfo(job, detailedPosition, company));

        Course course = courseRepository.save(new Course("언어", job));
        CourseDetail courseDetailJava = courseDetailRepository.save(new CourseDetail("Java", course));
        CourseDetail courseDetailKotlin = courseDetailRepository.save(new CourseDetail("Kotlin", course));
        CompanyRequiredAbility companyRequiredAbilityJava = companyRequiredAbilityRepository.save(new CompanyRequiredAbility("자바", companyInfo, courseDetailJava));
        CompanyRequiredAbility companyRequiredAbilityKotlin = companyRequiredAbilityRepository.save(new CompanyRequiredAbility("코틀린", companyInfo, courseDetailKotlin));

        //when
        List<CourseDetail> courseDetailList = courseDetailRepository.getCourseDetailList(course.getId(), company.getId(), job.getId(), detailedPosition.getId());

        //then
        Assertions.assertThat(courseDetailList.get(0).getCourse().getName()).isEqualTo("언어");
        Assertions.assertThat(courseDetailList.get(0).getName()).isEqualTo("Java");
        Assertions.assertThat(courseDetailList.get(1).getCourse().getId()).isEqualTo(course.getId());
        Assertions.assertThat(courseDetailList.get(1).getName()).isEqualTo("Kotlin");

    }

    @Test
    @DisplayName("코스, 회사, 직무, 서비스 정보에 대응되는 정보가 없을때 빈 리스트를 반환한다")
    void getCourseDetailList_not_exist_test() {
        //given
        Long notExistingCompanyId = 9999L;
        Company company = companyRepository.save(new Company("토스"));
        Job job = jobRepository.save(new Job("백엔드"));
        DetailedPosition detailedPosition = detailedPositionRepository.save(new DetailedPosition("Product", company));
        CompanyInfo companyInfo = companyInfoRepository.save(new CompanyInfo(job, detailedPosition, company));

        Course course = courseRepository.save(new Course("언어", job));
        CourseDetail courseDetailJava = courseDetailRepository.save(new CourseDetail("Java", course));
        CourseDetail courseDetailKotlin = courseDetailRepository.save(new CourseDetail("Kotlin", course));
        CompanyRequiredAbility companyRequiredAbilityJava = companyRequiredAbilityRepository.save(new CompanyRequiredAbility("자바", companyInfo, courseDetailJava));
        CompanyRequiredAbility companyRequiredAbilityKotlin = companyRequiredAbilityRepository.save(new CompanyRequiredAbility("코틀린", companyInfo, courseDetailKotlin));

        //when
        List<CourseDetail> courseDetailList = courseDetailRepository.getCourseDetailList(course.getId(), notExistingCompanyId, job.getId(), detailedPosition.getId());

        //then
        Assertions.assertThat(courseDetailList).isEmpty();

    }

    @Test
    @DisplayName("코스, 회사, 직무, 서비스 정보가 주어졌을때, CourseDetail 에 매칭되지 않은 회사요구역량이 존재하면, 회사요구역량에 대응되는 CourseDetail 리스트만 반환한다")
    void getCourseDetailList_not_matched_company_required_ability_test() {
        //given
        Company company = companyRepository.save(new Company("토스"));
        Job job = jobRepository.save(new Job("백엔드"));
        DetailedPosition detailedPosition = detailedPositionRepository.save(new DetailedPosition("Product", company));
        CompanyInfo companyInfo = companyInfoRepository.save(new CompanyInfo(job, detailedPosition, company));

        Course course = courseRepository.save(new Course("언어", job));
        CourseDetail courseDetailJava = courseDetailRepository.save(new CourseDetail("Java", course));
        CourseDetail courseDetailKotlin = courseDetailRepository.save(new CourseDetail("Kotlin", course));
        CompanyRequiredAbility companyRequiredAbilityJava = companyRequiredAbilityRepository.save(new CompanyRequiredAbility("자바", companyInfo, courseDetailJava));
        CompanyRequiredAbility companyRequiredAbilityKotlin = companyRequiredAbilityRepository.save(new CompanyRequiredAbility("코틀린", companyInfo, courseDetailKotlin));
        CompanyRequiredAbility companyRequiredAbilityPython = companyRequiredAbilityRepository.save(new CompanyRequiredAbility("파이썬", companyInfo, null));
        CompanyRequiredAbility companyRequiredAbilityRuby = companyRequiredAbilityRepository.save(new CompanyRequiredAbility("루비", companyInfo, null));

        //when
        List<CourseDetail> courseDetailList = courseDetailRepository.getCourseDetailList(course.getId(), company.getId(), job.getId(), detailedPosition.getId());

        //then
        Assertions.assertThat(courseDetailList.get(0).getCourse().getName()).isEqualTo("언어");
        Assertions.assertThat(courseDetailList.get(0).getName()).isEqualTo("Java");
        Assertions.assertThat(courseDetailList.get(1).getCourse().getId()).isEqualTo(course.getId());
        Assertions.assertThat(courseDetailList.get(1).getName()).isEqualTo("Kotlin");
    }
}