package io.devridge.api.domain.companyinfo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CompanyRequiredAbilityRepository extends JpaRepository<CompanyRequiredAbility, Long> {
    Page<CompanyRequiredAbility> findByCourseDetailIdIsNull(Pageable pageable);

    @Query("SELECT cra FROM CompanyRequiredAbility cra WHERE LOWER(REPLACE(cra.name, ' ', '')) = LOWER(REPLACE(:name, ' ', ''))")
    Optional<CompanyRequiredAbility> findByNameIgnoreCaseAndSpacesRemoved(String name);
}
