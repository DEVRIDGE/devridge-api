package io.devridge.api.dto.admin.company_info;


import io.devridge.api.domain.companyinfo.DetailedPosition;
import lombok.Getter;

@Getter
public class DetailedPositionDto {
    private final Long id;
    private final String name;

    public DetailedPositionDto(DetailedPosition detailedPosition) {
        this.id = detailedPosition.getId();
        this.name = detailedPosition.getName();
    }
}
