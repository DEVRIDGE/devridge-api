package io.devridge.api.domain.companyinfo;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompanyRequiredAbilityRepository extends JpaRepository<CompanyRequiredAbility, Long> {

    Optional<CompanyRequiredAbility> findByNameAndCompanyInfoId(String companyRequiredAbilityName, Long companyInfoId);
}
