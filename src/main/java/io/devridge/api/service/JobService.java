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

    public Optional<Job> findByName(String jobName) {
        return jobRepository.findByName(jobName);
    }

    public Job save(Job job) {
        return jobRepository.save(job);
    }

    public JobResponseDto jobList() {
        List<Job> jobList = jobRepository.findAll();

        return new JobResponseDto(jobList);
    }
}
