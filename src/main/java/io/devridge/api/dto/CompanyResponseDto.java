package io.devridge.api.dto;

import io.devridge.api.domain.company_job.Company;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class CompanyResponseDto {

    private List<CompanyDto> companies;

    public CompanyResponseDto(List<Company> companies) {
        this.companies = companies.stream().map(company -> new CompanyDto(company)).collect(Collectors.toList());
    }

    @Getter // Getter 붙여야 오류 안난다
    public class CompanyDto {
        private Long id;
        private String name;

        public CompanyDto(Company company) {
            this.id = company.getId();
            this.name = company.getName();
        }
    }
}
