package io.devridge.admin.dto.company_info;

import io.devridge.core.domain.company.Company;
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
