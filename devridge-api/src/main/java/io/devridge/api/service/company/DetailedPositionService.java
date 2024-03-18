package io.devridge.api.service.company;


import io.devridge.api.dto.company.DetailedPositionListDto;
import io.devridge.api.handler.ex.company.CompanyJobNotFoundException;
import io.devridge.api.repository.company.ApiDetailedPositionRepository;
import io.devridge.core.domain.company.DetailedPosition;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DetailedPositionService {

    private final JobService jobService;
    private final ApiDetailedPositionRepository detailedPositionRepository;

    public DetailedPositionListDto getDetailedPositionList(Long companyId, Long jobId) {
        validateCompanyJob(companyId, jobId);

        List<DetailedPosition> detailedPositionList = detailedPositionRepository.findByCompanyIdAndJobId(companyId, jobId);

        return new DetailedPositionListDto(detailedPositionList);
    }

    private void validateCompanyJob(Long companyId, Long jobId) {
        jobService.findCompanyJob(companyId, jobId).orElseThrow(CompanyJobNotFoundException::new);
    }
}