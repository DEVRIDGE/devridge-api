package io.devridge.api.service;

import io.devridge.api.domain.companyinfo.CompanyJobRepository;
import io.devridge.api.domain.companyinfo.DetailedPosition;
import io.devridge.api.domain.companyinfo.DetailedPositionRepository;
import io.devridge.api.dto.companyinfo.DetailedPositionResponseDto;
import io.devridge.api.handler.ex.CompanyJobNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DetailedPositionService {
    private final CompanyJobRepository companyJobRepository;
    private final DetailedPositionRepository detailedPositionRepository;

    //TODO DetailedPositionDto 만들고 서비스 로직 만들어야함
    public DetailedPositionResponseDto getDetailedPositionList(long companyId, long jobId) {
        validateCompanyJob(companyId, jobId);
        List<DetailedPosition> detailedPositionList = detailedPositionRepository.findByCompanyIdAndJobId(companyId, jobId);

        return new DetailedPositionResponseDto(detailedPositionList);
    }

    private void validateCompanyJob(long companyId, long jobId) {
        companyJobRepository.findByCompanyIdAndJobId(companyId, jobId)
                .orElseThrow(() -> new CompanyJobNotFoundException("회사와 직무에 일치 하는 정보가 없습니다."));
    }
}