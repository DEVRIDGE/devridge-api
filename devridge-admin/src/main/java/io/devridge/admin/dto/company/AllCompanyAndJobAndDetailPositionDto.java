package io.devridge.admin.dto.company;

import io.devridge.admin.dto.company_info.CompanyDto;
import io.devridge.admin.dto.company_info.JobDto;
import io.devridge.core.domain.company.Company;
import io.devridge.core.domain.company.Job;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class AllCompanyAndJobAndDetailPositionDto {
    List<CompanyDto> allCompanyList;
    List<JobDto> allJobList;

    public AllCompanyAndJobAndDetailPositionDto(List<Company> allCompanyList, List<Job> allJobList) {
        this.allCompanyList = allCompanyList.stream().map(CompanyDto::new).collect(Collectors.toList());
        this.allJobList = allJobList.stream().map(JobDto::new).collect(Collectors.toList());
    }
}
