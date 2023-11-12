package io.devridge.api.service;

import io.devridge.api.domain.companyinfo.Job;
import io.devridge.api.domain.companyinfo.JobRepository;
import io.devridge.api.dto.JobResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JobService {
    private final JobRepository jobRepository;
    private final CompanyService companyService;

    public Job save(Job job) {
        return jobRepository.save(job);
    }

    public JobResponseDto jobListByCompanyId(Long companyId) {

        companyService.throwsExceptionIfCompanyNotFound(companyId);

        List<Job> jobList = jobRepository.findByCompanyId(companyId);

        return new JobResponseDto(jobList);
    }
}
