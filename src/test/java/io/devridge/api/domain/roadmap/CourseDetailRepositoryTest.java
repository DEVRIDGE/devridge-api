package io.devridge.api.domain.roadmap;

import io.devridge.api.domain.companyinfo.CompanyInfoRepository;
import io.devridge.api.domain.companyinfo.CompanyRequiredAbilityRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CourseDetailRepositoryTest {
    @Autowired
    private CourseDetailRepository courseDetailRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private CompanyInfoRepository companyInfoRepository;
    @Autowired
    private CompanyRequiredAbilityRepository companyRequiredAbilityRepository;

    @Test
    @DisplayName("코스, 회사, 직무, 서비스 정보가 주어지면 해당하는 CourseDetail 리스트를 반환한다")
    void getCourseDetailList_success_test() {


    }
}