package io.devridge.api.domain.companyinfo;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompanyJobRepository extends JpaRepository<CompanyJob, Long> {
    Optional<CompanyJob> findByCompanyIdAndJobId(long companyId, long jobId);

    Optional<CompanyJob> findByCompanyAndJob(Company company, Job job);

}
