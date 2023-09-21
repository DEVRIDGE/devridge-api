package io.devridge.api.service;

import io.devridge.api.domain.companyinfo.CompanyInfo;
import io.devridge.api.domain.companyinfo.CompanyInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CompanyInfoService {

    private final CompanyInfoRepository companyInfoRepository;
    public void saveCompanyInfo(CompanyInfo companyInfo) {
        companyInfoRepository.save(companyInfo);
    }
}
