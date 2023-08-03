package io.devridge.api.config;

import io.devridge.api.domain.company_job.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@RequiredArgsConstructor
@Component
public class InitData {

    private final CompanyRepository companyRepository;
    private final JobRepository jobRepository;
    private final CompanyJobRepository companyJobRepository;

    @PostConstruct
    public void init() {
        Company company1 = makeCompany("토스증권");
        Job job1 = makeJob("백엔드");
        CompanyJob companyJob = mkaeCompanyJob(company1, job1);
    }

    private Company makeCompany(String name) {
        Company company = new Company(name, "");
        return companyRepository.save(company);
    }

    private Job makeJob(String name) {
        Job job = new Job(name);
        return jobRepository.save(job);
    }

    private CompanyJob mkaeCompanyJob(Company company, Job job) {
        CompanyJob companyJob = new CompanyJob(company, job);
        return companyJobRepository.save(companyJob);
    }

}
