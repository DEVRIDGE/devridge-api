package io.devridge.api.domain.companyinfo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CompanyRequiredAbilityRepository extends JpaRepository<CompanyRequiredAbility, Long> {

    Optional<CompanyRequiredAbility> findByName(String companyRequiredAbilityName);


//    //TODO CompanyRequiredAbilityIds를 CompanyInfoCompanyRequiredAbility에서 사용하도록 해야함
//    @Query("SELECT cra.courseDetail.id FROM CompanyRequiredAbility cra WHERE cra.companyInfo.id = :companyInfoId AND cra.courseDetail IS NOT NULL")
//    List<Long> findIdsByCompanyInfoId(@Param("companyInfoId") Long companyInfoId);
//
//    //TODO CompanyRequiredAbility가 아니라 CompanyInfoCompanyRequiredAbility 쓰도록 쿼리 바꿔야함
//    @Query("SELECT cra " +
//            "FROM CompanyRequiredAbility cra " +
//            "JOIN FETCH cra.companyInfo c " +
//            "JOIN FETCH c.company " +
//            "JOIN FETCH c.job " +
//            "JOIN FETCH c.detailedPosition " +
//            "WHERE cra.courseDetail IS NULL and c.job.id = :jobId")
//    List<CompanyRequiredAbility> findAllByCourseDetailIsNullFetch(@Param("jobId") Long jobId);

    List<CompanyRequiredAbility> findByCourseDetailId(Long courseDetailId);
}
