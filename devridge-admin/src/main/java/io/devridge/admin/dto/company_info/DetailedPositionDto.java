package io.devridge.admin.dto.company_info;


import io.devridge.core.domain.company.DetailedPosition;
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
