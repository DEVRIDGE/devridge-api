package io.devridge.api.dto.admin.company_info;

import io.devridge.api.domain.companyinfo.Company;
import io.devridge.api.domain.companyinfo.Job;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class NeededMakeCompanyInfoDto {
    List<CompanyDto> allCompanyList;
    List<JobDto> allJobList;

    public NeededMakeCompanyInfoDto(List<Company> allCompanyList, List<Job> allJobList) {
        this.allCompanyList = allCompanyList.stream().map(CompanyDto::new).collect(Collectors.toList());
        this.allJobList = allJobList.stream().map(JobDto::new).collect(Collectors.toList());
    }
}
