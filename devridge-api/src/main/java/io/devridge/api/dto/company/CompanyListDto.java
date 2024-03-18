package io.devridge.api.dto.company;

import io.devridge.core.domain.company.Company;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class CompanyListDto {

    private List<CompanyDto> companies;

    public CompanyListDto(List<Company> companies) {
        this.companies = companies.stream()
                .map(CompanyDto::new)
                .collect(Collectors.toList());
    }

    @Getter
    public class CompanyDto {
        private Long id;
        private String name;

        public CompanyDto(Company company) {
            this.id = company.getId();
            this.name = company.getName();
        }
    }
}
