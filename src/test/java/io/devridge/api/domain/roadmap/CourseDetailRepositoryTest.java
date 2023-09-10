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
        Company company = companyRepository.save(Company.builder().name("토스").build());
        Job job = jobRepository.save(Job.builder().name("백엔드").build());
        DetailedPosition detailedPosition = detailedPositionRepository.save(DetailedPosition.builder().name("Product").company(company).build());
        CompanyInfo companyInfo = companyInfoRepository.save(CompanyInfo.builder().job(job).detailedPosition(detailedPosition).company(company).build());

        Course course = courseRepository.save(Course.builder().name("언어").job(job).build());
        CourseDetail courseDetailJava = courseDetailRepository.save(CourseDetail.builder().name("Java").course(course).build());
        CourseDetail courseDetailKotlin = courseDetailRepository.save(CourseDetail.builder().name("Kotlin").course(course).build());
        CompanyRequiredAbility companyRequiredAbilityJava = companyRequiredAbilityRepository.save(CompanyRequiredAbility.builder().name("자바").companyInfo(companyInfo).courseDetail(courseDetailJava).build());
        CompanyRequiredAbility companyRequiredAbilityKotlin = companyRequiredAbilityRepository.save(CompanyRequiredAbility.builder().name("코틀린").companyInfo(companyInfo).courseDetail(courseDetailKotlin).build());

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
        Company company = companyRepository.save(Company.builder().name("토스").build());
        Job job = jobRepository.save(Job.builder().name("백엔드").build());
        DetailedPosition detailedPosition = detailedPositionRepository.save(DetailedPosition.builder().name("Product").company(company).build());
        CompanyInfo companyInfo = companyInfoRepository.save(CompanyInfo.builder().job(job).detailedPosition(detailedPosition).company(company).build());

        Course course = courseRepository.save(Course.builder().name("언어").job(job).build());
        CourseDetail courseDetailJava = courseDetailRepository.save(CourseDetail.builder().name("Java").course(course).build());
        CourseDetail courseDetailKotlin = courseDetailRepository.save(CourseDetail.builder().name("Kotlin").course(course).build());
        CompanyRequiredAbility companyRequiredAbilityJava = companyRequiredAbilityRepository.save(CompanyRequiredAbility.builder().name("자바").companyInfo(companyInfo).courseDetail(courseDetailJava).build());
        CompanyRequiredAbility companyRequiredAbilityKotlin = companyRequiredAbilityRepository.save(CompanyRequiredAbility.builder().name("코틀린").companyInfo(companyInfo).courseDetail(courseDetailKotlin).build());

        //when
        List<CourseDetail> courseDetailList = courseDetailRepository.getCourseDetailList(course.getId(), notExistingCompanyId, job.getId(), detailedPosition.getId());

        //then
        Assertions.assertThat(courseDetailList).isEmpty();

    }

    @Test
    @DisplayName("코스, 회사, 직무, 서비스 정보가 주어졌을때, CourseDetail 에 매칭되지 않은 회사요구역량이 존재하면, 회사요구역량에 대응되는 CourseDetail 리스트만 반환한다")
    void getCourseDetailList_not_matched_company_required_ability_test() {
        //given
        Company company = companyRepository.save(Company.builder().name("토스").build());
        Job job = jobRepository.save(Job.builder().name("백엔드").build());
        DetailedPosition detailedPosition = detailedPositionRepository.save(DetailedPosition.builder().name("Product").company(company).build());
        CompanyInfo companyInfo = companyInfoRepository.save(CompanyInfo.builder().job(job).detailedPosition(detailedPosition).company(company).build());

        Course course = courseRepository.save(Course.builder().name("언어").job(job).build());
        CourseDetail courseDetailJava = courseDetailRepository.save(CourseDetail.builder().name("Java").course(course).build());
        CourseDetail courseDetailKotlin = courseDetailRepository.save(CourseDetail.builder().name("Kotlin").course(course).build());
        CompanyRequiredAbility companyRequiredAbilityJava = companyRequiredAbilityRepository.save(CompanyRequiredAbility.builder().name("자바").companyInfo(companyInfo).courseDetail(courseDetailJava).build());
        CompanyRequiredAbility companyRequiredAbilityKotlin = companyRequiredAbilityRepository.save(CompanyRequiredAbility.builder().name("코틀린").companyInfo(companyInfo).courseDetail(courseDetailKotlin).build());
        CompanyRequiredAbility companyRequiredAbilityPython = companyRequiredAbilityRepository.save(CompanyRequiredAbility.builder().name("파이썬").companyInfo(companyInfo).build());
        CompanyRequiredAbility companyRequiredAbilityRuby = companyRequiredAbilityRepository.save(CompanyRequiredAbility.builder().name("파이썬").companyInfo(companyInfo).build());

        //when
        List<CourseDetail> courseDetailList = courseDetailRepository.getCourseDetailList(course.getId(), company.getId(), job.getId(), detailedPosition.getId());

        //then
        Assertions.assertThat(courseDetailList.get(0).getCourse().getName()).isEqualTo("언어");
        Assertions.assertThat(courseDetailList.get(0).getName()).isEqualTo("Java");
        Assertions.assertThat(courseDetailList.get(1).getCourse().getId()).isEqualTo(course.getId());
        Assertions.assertThat(courseDetailList.get(1).getName()).isEqualTo("Kotlin");
    }
}