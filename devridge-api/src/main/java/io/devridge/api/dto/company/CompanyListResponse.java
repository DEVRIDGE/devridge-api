package io.devridge.api.dto.company;

import io.devridge.core.domain.company.Company;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class CompanyListResponse {

    private final List<CompanyDto> companies;

    public CompanyListResponse(List<Company> companies) {
        this.companies = companies.stream()
                .map(CompanyDto::new)
                .collect(Collectors.toList());
    }

    @Getter
    public static class CompanyDto {
        private final Long id;
        private final String name;

        public CompanyDto(Company company) {
            this.id = company.getId();
            this.name = company.getName();
        }
    }
}
