package io.devridge.api.service;


import io.devridge.api.domain.companyinfo.CompanyInfoCompanyRequiredAbility;
import io.devridge.api.domain.companyinfo.CompanyInfoCompanyRequiredAbilityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CompanyInfoCompanyRequiredAbilityService {
    private final CompanyInfoCompanyRequiredAbilityRepository companyInfoCompanyRequiredAbilityRepository;

    public Optional<CompanyInfoCompanyRequiredAbility> findByCompanyInfoIdAndCompanyRequiredAbilityId(Long companyInfoId, Long companyRequiredAbilityId) {
        return companyInfoCompanyRequiredAbilityRepository.findByCompanyInfoIdAndCompanyRequiredAbilityId(companyInfoId, companyRequiredAbilityId);
    }

    public CompanyInfoCompanyRequiredAbility save(CompanyInfoCompanyRequiredAbility companyInfoCompanyRequiredAbility) {
        return companyInfoCompanyRequiredAbilityRepository.save(companyInfoCompanyRequiredAbility);
    }
}
