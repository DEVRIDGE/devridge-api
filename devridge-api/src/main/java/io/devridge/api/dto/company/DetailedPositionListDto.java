package io.devridge.api.dto.company;

import io.devridge.core.domain.company.DetailedPosition;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class DetailedPositionListDto {
    private List<DetailedPositionDto> detailedPositionDtos;

    public DetailedPositionListDto(List<DetailedPosition> detailedPositions) {
        this.detailedPositionDtos = detailedPositions.stream().map(DetailedPositionDto::new).collect(Collectors.toList());
    }

    @Getter
    public class DetailedPositionDto {
        private Long id;
        private String name;

        public DetailedPositionDto(DetailedPosition detailedPosition) {
            this.id = detailedPosition.getId();
            this.name = detailedPosition.getName();
        }
    }
}
