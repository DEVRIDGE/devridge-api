package io.devridge.api.repository.recruitment;

import io.devridge.core.domain.recruitment.RecruitmentInfo;
import io.devridge.core.domain.recruitment.RecruitmentInfoRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ApiRecruitmentInfoRepository extends RecruitmentInfoRepository {
    @Query("SELECT ri FROM RecruitmentInfo ri " +
            "JOIN FETCH ri.job j " +
            "JOIN FETCH ri.detailedPosition dp " +
            "JOIN FETCH dp.company c " +
            "WHERE ri.job.id = :jobId AND ri.detailedPosition.id = :detailedPositionId")
    Optional<RecruitmentInfo> findByRecruitmentInfoByJobIdAndDetailedPositionIdWithFetch(@Param("jobId") Long jobId, @Param("detailedPositionId") Long detailedPositionId);

    Optional<RecruitmentInfo> findByCompanyIdAndJobIdAndDetailedPositionId(Long companyId, Long jobId, Long detailedPositionId);
}
