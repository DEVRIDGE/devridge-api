package io.devridge.api.service.admin;

import io.devridge.api.domain.companyinfo.*;
import io.devridge.api.dto.companyinfo.CompanyInfoForm;
import io.devridge.api.handler.ex.ExistingCompanyInfoException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Transactional
@Service
public class AdminCompanyInfoService {

    /**
     * CompanyInfo가 전달되면 회사정보 테이블에 CompanyInfo가 저장되고
     * 직무, 서비스종류, 회사 테아블에 회사정보 내의 정보가 전달된다.
     * 그리고 회사와 직무,직무와 서비스종류,서비스종류와 회사를 연관시킨다.
     */
/*
    public void transferCompanyInfoToAssociatedTable(CompanyInfoForm companyInfoForm) {

        //회사를 저장한다. 이미 존재하면 이미 있는 회사를 가져온다.
        Company targetCompany = getCompany(companyInfoForm);

        //직무를 저장한다. 이미 존재하면 이미 있는 직무를 가져온다.
        Job targetJob = getJob(companyInfoForm);


        //서비스 종류를 저장한다. 이미 존재하면 이미 있는 서비스 종류를 가져온다.
        DetailedPosition targetDetailedPosition = getDetailedPosition(companyInfoForm, targetCompany);

        //회사와 직무를 연관시킨다.
        associateCompanyJob(targetCompany, targetJob);

        //직무와 서비스종류를 연관시킨다.
        associateJobDetailedPosition(targetJob, targetDetailedPosition);

        //서비스 종류와 회사는 위에서 서비스 종류를 저장할 때 연관되었으므로 없어도 된다.

        //회사정보를 저장한다. 이미 해당하는 회사, 직무, 서비스 종류의 회사 정보가 있으면 예외가 발생한다.
        saveCompanyInfo(companyInfoForm, targetCompany, targetJob, targetDetailedPosition);
    }

    private Company getCompany(CompanyInfoForm companyInfoForm) {
        Company targetCompany;
        Optional<Company> foundCompany = companyService.findByName(companyInfoForm.getCompanyName());
        if(foundCompany.isEmpty()) {
            Company newCompany = Company.builder()
                    .name(companyInfoForm.getCompanyName())
                    .build();
            targetCompany = companyService.save(newCompany);
        } else {
            targetCompany = foundCompany.get();
        }
        return targetCompany;
    }

    private Job getJob(CompanyInfoForm companyInfoForm) {
        Job targetJob;
        Optional<Job> foundJob = jobService.findByName(companyInfoForm.getJobName());
        if(foundJob.isEmpty()) {
            Job newJob = Job.builder()
                    .name(companyInfoForm.getJobName())
                    .build();
            targetJob = jobService.save(newJob);
        } else {
            targetJob = foundJob.get();
        }
        return targetJob;
    }

    private DetailedPosition getDetailedPosition(CompanyInfoForm companyInfoForm, Company targetCompany) {
        DetailedPosition targetDetailedPosition;
        Optional<DetailedPosition> foundDetailedPosition = detailedPositionService.findByNameAndCompanyId(companyInfoForm.getDetailedPositionName(), targetCompany.getId());
        if(foundDetailedPosition.isEmpty()) {
            DetailedPosition newDetailedPosition = DetailedPosition.builder()
                    .name(companyInfoForm.getDetailedPositionName())
                    .company(targetCompany)
                    .build();
            targetDetailedPosition = newDetailedPosition;
        } else {
            targetDetailedPosition = foundDetailedPosition.get();
        }
        return targetDetailedPosition;
    }

    private void associateCompanyJob(Company targetCompany, Job targetJob) {
        if(adminCompanyJobService.findByCompanyIdAndJobId(targetCompany.getId(), targetJob.getId()).isEmpty()) {
            CompanyJob newCompanyJob = CompanyJob.builder()
                    .company(targetCompany)
                    .job(targetJob)
                    .build();

            adminCompanyJobService.save(newCompanyJob);
        }
    }

    private void associateJobDetailedPosition(Job targetJob, DetailedPosition targetDetailedPosition) {
        if(jobDetailedPositionService.findByJobIdAndDetailedPositionId(targetJob.getId(), targetDetailedPosition.getId()).isEmpty()) {
            JobDetailedPosition newJobDetailedPosition = JobDetailedPosition.builder()
                    .job(targetJob)
                    .detailedPosition(targetDetailedPosition)
                    .build();

            //jobDetailedPositionService.save(newJobDetailedPosition);
        }
    }

    private void saveCompanyInfo(CompanyInfoForm companyInfoForm, Company targetCompany, Job targetJob, DetailedPosition targetDetailedPosition) {
        Optional<CompanyInfo> foundCompanyInfo = companyInfoRepository.findByCompanyIdAndJobIdAndDetailedPositionId(targetCompany.getId(), targetJob.getId(), targetDetailedPosition.getId());

        if(foundCompanyInfo.isPresent()) {
            throw new ExistingCompanyInfoException();
        }

        CompanyInfo newCompanyInfo = CompanyInfo.builder()
                .company(targetCompany)
                .job(targetJob)
                .detailedPosition(targetDetailedPosition)
                .content(companyInfoForm.getContent())
                .build();
        companyInfoRepository.save(newCompanyInfo);
    }
    */
}
