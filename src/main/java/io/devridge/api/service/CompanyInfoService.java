package io.devridge.api.service;

import io.devridge.api.domain.companyinfo.*;
import io.devridge.api.handler.ex.CompanyInfoNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CompanyInfoService {
    private final CompanyInfoRepository companyInfoRepository;

    public CompanyInfo validateCompanyInfo(Long companyId, Long jobId, Long detailedPositionId) {
        return companyInfoRepository.findByCompanyIdAndJobIdAndDetailedPositionId(companyId, jobId, detailedPositionId).orElseThrow(() -> new CompanyInfoNotFoundException("해당하는 회사 정보를 찾을 수 없습니다."));
    }
}
