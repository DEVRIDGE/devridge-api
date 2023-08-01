package io.devridge.api.dto;

import io.devridge.api.domain.company_job.Company;

import java.util.List;

public class CompanyResponseDto {

    private List<CompanyDto> companies;


    public class CompanyDto {
        private Long id;
        private String name;

        public CompanyDto(Company company) {
            this.id = company.getId();
            this.name = company.getName();
        }
    }
}
