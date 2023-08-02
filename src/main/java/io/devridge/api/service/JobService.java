package io.devridge.api.service;

import io.devridge.api.domain.company_job.Company;
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

    /**
     * 테스트용 코드
     */
//    @PostConstruct
//    public void init() {
//        Job job1 = new Job(1L, "test1");
//        Job job2 = new Job(2L, "test2");
//        jobRepository.save(job1);
//        jobRepository.save(job2);
//    }
}
