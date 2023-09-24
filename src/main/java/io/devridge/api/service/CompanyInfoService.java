package io.devridge.api.service;

import io.devridge.api.domain.companyinfo.CompanyInfo;
import io.devridge.api.domain.companyinfo.CompanyInfoRepository;
import io.devridge.api.handler.ex.ExistingCompanyInfoException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CompanyInfoService {

    private final CompanyInfoRepository companyInfoRepository;

    /**
     * CompanyInfo가 전달되면 회사정보 테이블에 CompanyInfo가 저장되고
     * 직무, 서비스종류, 회사 테아블에 회사정보 내의 정보가 전달된다.
     * 그리고 회사와 직무,직무와 서비스종류,서비스종류와 회사를 연관시킨다.
     */

    public void transferCompanyInfoToAssociatedTable(CompanyInfo companyInfo) {
        saveCompanyInfo(companyInfo);

        //회사를 저장한다
        //직무를 저장한다.
        //서비스 종류를 저장한다.

        //회사와 직무를 연관시킨다.
        //직무와 서비스종류를 연관시킨다.
        //서비스 종류와 회사를 연관시킨다.

    }

    public void saveCompanyInfo(CompanyInfo companyInfo) {

        Optional<CompanyInfo> existingCompanyInfo = companyInfoRepository.findByCompanyIdAndJobIdAndDetailedPositionId(companyInfo.getCompany().getId(), companyInfo.getJob().getId(), companyInfo.getDetailedPosition().getId());

        if(existingCompanyInfo.isPresent()) {
            throw new ExistingCompanyInfoException();
        }

        companyInfoRepository.save(companyInfo);
    }
}
