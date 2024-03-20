package io.devridge.admin.repository.company;

import io.devridge.core.domain.company.Company;
import io.devridge.core.domain.company.CompanyJob;
import io.devridge.core.domain.company.CompanyJobRepository;
import io.devridge.core.domain.company.Job;

import java.util.Optional;

public interface AdminCompanyJobRepository extends CompanyJobRepository {
    Optional<CompanyJob> findByCompanyAndJob(Company company, Job job);
}
