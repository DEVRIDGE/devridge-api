package io.devridge.api.domain.companyinfo;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompanyInfoCompanyRequiredAbilityRepository extends JpaRepository<CompanyInfoCompanyRequiredAbility, Long> {

    Optional<CompanyInfoCompanyRequiredAbility> findByCompanyInfoIdAndCompanyRequiredAbilityId(Long companyInfoId, Long CompanyRequiredAbilityId);
}
