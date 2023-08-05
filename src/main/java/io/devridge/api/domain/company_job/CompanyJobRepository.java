package io.devridge.api.domain.company_job;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompanyJobRepository extends JpaRepository<CompanyJob, Long> {

    Optional<CompanyJob> findByCompanyIdAndJobId(long companyId, long jobId);

}
