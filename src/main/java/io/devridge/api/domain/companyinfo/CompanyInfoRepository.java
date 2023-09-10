package io.devridge.api.domain.companyinfo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CompanyInfoRepository extends JpaRepository<CompanyInfo, Long> {

    @Query("SELECT ci FROM CompanyInfo ci " +
            "JOIN FETCH ci.company c " +
            "JOIN FETCH ci.job j " +
            "WHERE ci.company.id = :companyId AND ci.job.id = :jobId AND ci.detailedPosition.id = :detailPositionId")
    Optional<CompanyInfo> findCompanyInfoByCompanyIdAndJobIdAndDetailedPositionId(long companyId, long jobId, long detailPositionId);

}
