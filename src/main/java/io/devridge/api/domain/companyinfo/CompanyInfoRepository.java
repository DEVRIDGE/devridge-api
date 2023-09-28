package io.devridge.api.domain.companyinfo;

import io.devridge.api.dto.admin.CompanyInfoDto;
import io.devridge.api.dto.admin.CompanyInfoIdDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CompanyInfoRepository extends JpaRepository<CompanyInfo, Long> {

    @Query("SELECT ci FROM CompanyInfo ci " +
            "JOIN FETCH ci.company c " +
            "JOIN FETCH ci.job j " +
            "WHERE ci.company.id = :companyId AND ci.job.id = :jobId AND ci.detailedPosition.id = :detailedPositionId")
    Optional<CompanyInfo> findByCompanyIdAndJobIdAndDetailedPositionIdWithFetchJoin(long companyId, long jobId, long detailedPositionId);

    Optional<CompanyInfo> findByCompanyIdAndJobIdAndDetailedPositionId(long companyId, long jobId, long detailedPositionId);


    @Query("SELECT new io.devridge.api.dto.admin.CompanyInfoDto(c.name, j.name, dp.name, MAX(r.id)) FROM CompanyInfo ci " +
            "JOIN ci.company c " +
            "JOIN ci.job j " +
            "JOIN ci.detailedPosition dp " +
            "LEFT JOIN Roadmap r ON ci.id = r.companyInfo.id " +
            "GROUP BY c.name, j.name, dp.name")
    List<CompanyInfoDto> findByAllWithRoadmap();

    @Query("SELECT new io.devridge.api.dto.admin.CompanyInfoIdDto(ci.id, c.id, j.id, dp.id, MAX(r.id)) FROM CompanyInfo ci " +
            "JOIN ci.company c " +
            "JOIN ci.job j " +
            "JOIN ci.detailedPosition dp " +
            "LEFT JOIN Roadmap r ON ci.id = r.companyInfo.id " +
            "GROUP BY c.name, j.name, dp.name")
    List<CompanyInfoIdDto> findByAllIdWithRoadmap();
}
