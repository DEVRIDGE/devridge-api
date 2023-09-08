package io.devridge.api.domain.companyinfo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DetailedPositionRepository extends JpaRepository<DetailedPosition, Long> {

    @Query("select dp from DetailedPosition dp " +
            "JOIN JobDetailedPosition jdp ON jdp.detailedPosition.id = dp.id " +
            "where dp.company.id = :companyId and jdp.job.id = :jobId")
    List<DetailedPosition> findByCompanyIdAndJobId(@Param("companyId") long companyId, @Param("jobId") long jobId);
}
