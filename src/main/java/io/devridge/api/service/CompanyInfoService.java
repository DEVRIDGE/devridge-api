package io.devridge.api.service;

import io.devridge.api.domain.companyinfo.*;
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
    private final JobService jobService;
    private final DetailedPositionService detailedPositionService;

    private final CompanyJobService companyJobService;
    private final JobDetailedPositionService jobDetailedPositionService;


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

        //직무를 저장한다. 이미 존재하면 이미 있는 직무를 가져온다.
        Job targetJob;
        Optional<Job> foundJob = jobService.findByName(companyInfoDto.getJobName());
        if(foundJob.isEmpty()) {
            Job newJob = Job.builder()
                    .name(companyInfoDto.getJobName())
                    .build();
            targetJob = jobService.save(newJob);
        } else {
            targetJob = foundJob.get();
        }


        //서비스 종류를 저장한다. 이미 존재하면 이미 있는 서비스 종류를 가져온다.
        DetailedPosition targetDetailedPosition;
        Optional<DetailedPosition> foundDetailedPosition = detailedPositionService.findByNameAndCompanyId(companyInfoDto.getDetailedPositionName(), targetCompany.getId());
        if(foundDetailedPosition.isEmpty()) {
            DetailedPosition newDetailedPosition = DetailedPosition.builder()
                    .name(companyInfoDto.getDetailedPositionName())
                    .company(targetCompany)
                    .build();
            targetDetailedPosition = newDetailedPosition;
        } else {
            targetDetailedPosition = foundDetailedPosition.get();
        }

        //회사와 직무를 연관시킨다.
        if(companyJobService.findByCompanyIdAndJobId(targetCompany.getId(), targetJob.getId()).isEmpty()) {
            CompanyJob newCompanyJob = CompanyJob.builder()
                    .company(targetCompany)
                    .job(targetJob)
                    .build();

            companyJobService.save(newCompanyJob);
        }

        //직무와 서비스종류를 연관시킨다.
        if(jobDetailedPositionService.findByJobIdAndDetailedPositionId(targetJob.getId(), targetDetailedPosition.getId()).isEmpty()) {
            JobDetailedPosition newJobDetailedPosition = JobDetailedPosition.builder()
                    .job(targetJob)
                    .detailedPosition(targetDetailedPosition)
                    .build();

            jobDetailedPositionService.save(newJobDetailedPosition);
        }

        //서비스 종류와 회사는 위에서 서비스 종류를 저장할 때 연관되었으므로 없어도 된다.

        //회사정보를 저정한다.
        Optional<CompanyInfo> foundCompanyInfo = companyInfoRepository.findByCompanyIdAndJobIdAndDetailedPositionId(targetCompany.getId(), targetJob.getId(), targetDetailedPosition.getId());

        if(foundCompanyInfo.isPresent()) {
            throw new ExistingCompanyInfoException();
        }

        CompanyInfo newCompanyInfo = CompanyInfo.builder()
                .company(targetCompany)
                .job(targetJob)
                .detailedPosition(targetDetailedPosition)
                .content(companyInfoDto.getContent())
                .build();
        companyInfoRepository.save(newCompanyInfo);
    }
}
