package io.devridge.admin.service.company;

import io.devridge.admin.dto.company.AllCompanyAndJobAndDetailPositionDto;
import io.devridge.admin.repository.company.AdminCompanyJobRepository;
import io.devridge.admin.repository.company.AdminCompanyRepository;
import io.devridge.core.domain.company.Company;
import io.devridge.core.domain.company.CompanyJob;
import io.devridge.core.domain.company.Job;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AdminCompanyService {

    private final AdminJobService jobService;

    private final AdminCompanyRepository companyRepository;

    private final AdminCompanyJobRepository companyJobRepository;

    @Transactional(readOnly = true)
    public AllCompanyAndJobAndDetailPositionDto getCompanyAndJobAndDetailPositionList() {
        List<Company> allCompanyList = getAllCompanyWithDetailPosition();
        List<Job> allJobList = getAllJob();

        return new AllCompanyAndJobAndDetailPositionDto(allCompanyList, allJobList);
    }

    @Transactional
    public Company findCompanyOrSave(String companyName) {
        return companyRepository.findByNameByFetch(companyName)
                .orElseGet(() -> companyRepository.save(Company.builder().name(companyName).build()));
    }

    @Transactional
    public void associateCompanyAndJob(Company company, Job job) {
        companyJobRepository.findByCompanyAndJob(company, job)
                .orElseGet(() -> companyJobRepository.save(CompanyJob.builder().company(company).job(job).build()));
    }

    private List<Job> getAllJob() {
        return jobService.getAllJobs();
    }

    private List<Company> getAllCompanyWithDetailPosition() {
        return companyRepository.findAllByFetch();
    }
}
