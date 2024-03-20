package io.devridge.api.service.company;

import io.devridge.api.dto.company.JobListResponse;
import io.devridge.api.handler.ex.company.CompanyNotFoundException;
import io.devridge.api.repository.company.ApiCompanyJobRepository;
import io.devridge.core.domain.company.Company;
import io.devridge.core.domain.company.CompanyJob;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@Service
public class JobService {

    private final CompanyService companyService;

    private final ApiCompanyJobRepository companyJobRepository;

    public JobListResponse getJobListByCompanyId(long companyId) {
        Company company = companyService.findCompany(companyId).orElseThrow(CompanyNotFoundException::new);
        List<CompanyJob> companyJobs = companyJobRepository.getCompanyJobByCompany(company);

        return new JobListResponse(companyJobs);
    }

    Optional<CompanyJob> findCompanyJob(long companyId, long jobId) {
        return companyJobRepository.findByCompanyIdAndJobId(companyId, jobId);
    }
}
