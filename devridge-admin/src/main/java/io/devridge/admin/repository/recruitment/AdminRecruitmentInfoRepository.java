package io.devridge.admin.repository.recruitment;

import io.devridge.core.domain.company.Company;
import io.devridge.core.domain.company.DetailedPosition;
import io.devridge.core.domain.company.Job;
import io.devridge.core.domain.recruitment.RecruitmentInfo;
import io.devridge.core.domain.recruitment.RecruitmentInfoRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AdminRecruitmentInfoRepository extends RecruitmentInfoRepository {

    @Query("SELECT ri FROM RecruitmentInfo ri " +
            "JOIN FETCH ri.company c " +
            "JOIN FETCH ri.job j " +
            "WHERE ri.company = :company AND ri.job = :job AND ri.detailedPosition = :detailedPosition")
    Optional<RecruitmentInfo> findByCompanyAndJobAndDetailedPositionWithFetch(@Param("company") Company company, @Param("job") Job job, @Param("detailedPosition") DetailedPosition detailedPosition);
}
