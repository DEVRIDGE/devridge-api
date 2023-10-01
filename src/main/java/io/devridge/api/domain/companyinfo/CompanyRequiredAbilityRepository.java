package io.devridge.api.domain.companyinfo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CompanyRequiredAbilityRepository extends JpaRepository<CompanyRequiredAbility, Long> {

    Optional<CompanyRequiredAbility> findByNameAndCompanyInfoId(String companyRequiredAbilityName, Long companyInfoId);

    @Query("SELECT cra.courseDetail.id FROM CompanyRequiredAbility cra WHERE cra.companyInfo.id = :companyInfoId AND cra.courseDetail IS NOT NULL")
    List<Long> findIdsByCompanyInfoId(@Param("companyInfoId") Long companyInfoId);

    @Query("SELECT cra " +
            "FROM CompanyRequiredAbility cra " +
            "JOIN FETCH cra.companyInfo c " +
            "JOIN FETCH c.company " +
            "JOIN FETCH c.job " +
            "JOIN FETCH c.detailedPosition " +
            "WHERE cra.courseDetail IS NULL")
    List<CompanyRequiredAbility> findAllByCourseDetailIsNullFetch();

    @Query("SELECT cra " +
            "FROM CompanyRequiredAbility cra " +
            "WHERE cra.courseDetail IS NULL")
    List<CompanyRequiredAbility> findAllByCourseDetailIsNull();

    List<CompanyRequiredAbility> findByCourseDetailId(Long courseDetailId);
}
