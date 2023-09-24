package io.devridge.api.service;

import io.devridge.api.domain.companyinfo.CompanyJob;
import io.devridge.api.domain.companyinfo.CompanyJobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CompanyJobService {
    private final CompanyJobRepository companyJobRepository;

    Optional<CompanyJob> findByCompanyIdAndJobId(Long companyId, Long jobId) {
        return companyJobRepository.findByCompanyIdAndJobId(companyId, jobId);
    }

    CompanyJob save(CompanyJob companyJob) {
        return companyJobRepository.save(companyJob);
    }
}
