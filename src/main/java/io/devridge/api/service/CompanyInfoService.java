package io.devridge.api.service;

import io.devridge.api.domain.companyinfo.Company;
import io.devridge.api.domain.companyinfo.CompanyInfo;
import io.devridge.api.domain.companyinfo.CompanyInfoRepository;
import io.devridge.api.dto.companyinfo.CompanyInfoDto;
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
    private final CompanyService companyService;

    /**
     * CompanyInfo가 전달되면 회사정보 테이블에 CompanyInfo가 저장되고
     * 직무, 서비스종류, 회사 테아블에 회사정보 내의 정보가 전달된다.
     * 그리고 회사와 직무,직무와 서비스종류,서비스종류와 회사를 연관시킨다.
     */

    public void transferCompanyInfoToAssociatedTable(CompanyInfoDto companyInfoDto) {

        //회사를 저장한다. 이미 존재하면 이미 있는 회사를 가져온다.
        Company targetCompany;
        Optional<Company> foundCompany = companyService.findByName(companyInfoDto.getCompanyName());
        if(foundCompany.isEmpty()) {
            Company newCompany = Company.builder()
                    .name(companyInfoDto.getCompanyName())
                    .build();
            targetCompany = companyService.save(newCompany);
        } else {
            targetCompany = foundCompany.get();
        }

        //직무를 저장한다. 이미 존재하면 이미 있는 회사를 가져온다.

        //서비스 종류를 저장한다. 이미 존재하면 이미 있는 회사를 가져온다.

        //회사와 직무를 연관시킨다.

        //직무와 서비스종류를 연관시킨다.

        //서비스 종류와 회사를 연관시킨다.

        //회사 정보를 저정한다.
//        saveCompanyInfo(companyInfo);

    }

    public void saveCompanyInfo(CompanyInfo companyInfo) {

        CompanyInfo companyInfo1 = companyInfoRepository.findByCompanyIdAndJobIdAndDetailedPositionId(companyInfo.getCompany().getId(), companyInfo.getJob().getId(), companyInfo.getDetailedPosition().getId()).orElseThrow(() -> new ExistingCompanyInfoException());

        companyInfoRepository.save(companyInfo);
    }
}
