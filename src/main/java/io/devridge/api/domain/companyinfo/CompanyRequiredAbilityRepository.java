package io.devridge.api.domain.companyinfo;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CompanyRequiredAbilityRepository extends JpaRepository<CompanyRequiredAbility, Long> {

    Optional<CompanyRequiredAbility> findByName(String companyRequiredAbilityName);

//
//    @Query("SELECT cra.courseDetail.id FROM CompanyRequiredAbility cra " +
//            "JOIN CompanyInfoCompanyRequiredAbility cicra ON cra.id = cicra.companyRequiredAbility.id " +
//            "WHERE cicra.companyInfo.id = :companyInfoId AND cra.courseDetail IS NOT NULL")
//    List<Long> findIdsByCompanyInfoId(@Param("companyInfoId") Long companyInfoId);

    List<CompanyRequiredAbility> findByCourseDetailIdIsNull();

    List<CompanyRequiredAbility> findByCourseDetailId(Long courseDetailId);
}
