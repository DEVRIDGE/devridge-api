package io.devridge.admin.service.company;

import io.devridge.admin.handler.ex.AdminJobException;
import io.devridge.admin.repository.company.AdminJobDetailedPositionRepository;
import io.devridge.admin.repository.company.AdminJobRepository;
import io.devridge.core.domain.company.DetailedPosition;
import io.devridge.core.domain.company.Job;
import io.devridge.core.domain.company.JobDetailedPosition;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AdminJobService {

    private final AdminJobRepository jobRepository;
    private final AdminJobDetailedPositionRepository jobDetailedPositionRepository;

    @Transactional(readOnly = true)
    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Job findByName(String jobName) {
        return jobRepository.findByName(jobName)
                .orElseThrow(AdminJobException.AdminJobNotFoundException::new);
    }

    @Transactional
    public void associateJobAndDetailedPosition(Job job, DetailedPosition detailedPosition) {
        jobDetailedPositionRepository.findByJobAndDetailedPosition(job, detailedPosition)
                .orElseGet(() -> jobDetailedPositionRepository.save(JobDetailedPosition.builder().job(job).detailedPosition(detailedPosition).build()));
    }
}
