package io.devridge.api.service.admin;

import io.devridge.api.domain.companyinfo.*;
import io.devridge.api.dto.admin.company_info.CompanyRequiredAbilityDto;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Service
public class AdminCompanyRequiredAbilityService {

    private final CompanyRequiredAbilityRepository companyRequiredAbilityRepository;

    @Transactional(readOnly = true)
    public Page<CompanyRequiredAbilityDto> getCompanyInfoCompanyRequiredListWithCourseDetailIsNull(Pageable pageable) {
        Page<CompanyRequiredAbility> companyRequiredAbilityPage = companyRequiredAbilityRepository.findByCourseDetailIdIsNull(pageable);

        return companyRequiredAbilityPage.map(entity -> new CompanyRequiredAbilityDto(entity.getId(), entity.getName()));
    }
}
