package io.devridge.api.service;

import io.devridge.api.domain.companyinfo.JobDetailedPosition;
import io.devridge.api.domain.companyinfo.JobDetailedPositionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class JobDetailedPositionService {

    private final JobDetailedPositionRepository jobDetailedPositionRepository;

    public Optional<JobDetailedPosition> findByJobIdAndDetailedPositionId(Long jobId, Long detailedPositionId) {
        return jobDetailedPositionRepository.findByJobIdAndDetailedPositionId(jobId, detailedPositionId);
    }

    public JobDetailedPosition save(JobDetailedPosition jobDetailedPosition) {
        return jobDetailedPositionRepository.save(jobDetailedPosition);
    }
}
