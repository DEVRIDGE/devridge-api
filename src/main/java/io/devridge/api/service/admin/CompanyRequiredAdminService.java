package io.devridge.api.service.admin;

import io.devridge.api.domain.companyinfo.*;
import io.devridge.api.domain.roadmap.CourseDetail;
import io.devridge.api.domain.roadmap.CourseDetailRepository;
import io.devridge.api.dto.admin.company_info.CompanyRequiredAbilityDto;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@Service
public class CompanyRequiredAdminService {

    private final CompanyRequiredAbilityRepository companyRequiredAbilityRepository;
    private final CourseDetailRepository courseDetailRepository;

    @Transactional(readOnly = true)
    public Page<CompanyRequiredAbilityDto> getCompanyInfoCompanyRequiredListWithCourseDetailIsNull(Pageable pageable) {
        Page<CompanyRequiredAbility> companyRequiredAbilityPage = companyRequiredAbilityRepository.findByCourseDetailIdIsNull(pageable);

        return companyRequiredAbilityPage.map(entity -> new CompanyRequiredAbilityDto(entity.getId(), entity.getName()));
    }

    @Transactional
    public void matchRequiredAbilityWithCourseDetailId() {
        List<CompanyRequiredAbility> companyRequiredAbilityList = companyRequiredAbilityRepository.findByCourseDetailIdIsNull();
        for (CompanyRequiredAbility companyRequiredAbility : companyRequiredAbilityList) {
            Optional<CourseDetail> courseDetailOptional = courseDetailRepository.findByName(companyRequiredAbility.getName());
            if (courseDetailOptional.isEmpty()) {
                continue;
            }
            companyRequiredAbility.changeCourseDetail(courseDetailOptional.get());
        }
    }

    private String processStringCutting(String name) {
        // 모든 공백 제거
        String noSpaces = name.replaceAll("\\s+", "");

        // 영어 문자는 소문자로 변환하고, 다른 문자는 그대로 유지
        StringBuilder result = new StringBuilder();
        for (char c : noSpaces.toCharArray()) {
            if (Character.isAlphabetic(c) && Character.isUpperCase(c)) {
                result.append(Character.toLowerCase(c));
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }
}
