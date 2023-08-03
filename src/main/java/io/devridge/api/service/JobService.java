package io.devridge.api.service;

import io.devridge.api.domain.company_job.Job;
import io.devridge.api.domain.company_job.JobRepository;
import io.devridge.api.dto.JobResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JobService {
    private final JobRepository jobRepository;

    public JobResponseDto jobList() {
        List<Job> jobList = jobRepository.findAll();

        return new JobResponseDto(jobList);
    }
}
