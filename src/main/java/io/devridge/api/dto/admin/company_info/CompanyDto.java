package io.devridge.api.dto.admin.company_info;

import io.devridge.api.domain.companyinfo.Company;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class CompanyDto {
    private final Long id;
    private final String name;
    private final List<DetailedPositionDto> detailedPositionDtoList;

    public CompanyDto(Company company) {
        this.id = company.getId();
        this.name = company.getName();
        this.detailedPositionDtoList = company.getDetailedPositionList()
                .stream().map(DetailedPositionDto::new).collect(Collectors.toList());
    }
}
