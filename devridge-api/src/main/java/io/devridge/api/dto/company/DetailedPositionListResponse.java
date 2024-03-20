package io.devridge.api.dto.company;

import io.devridge.core.domain.company.DetailedPosition;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class DetailedPositionListResponse {
    private final List<DetailedPositionDto> detailedPositionDtos;

    public DetailedPositionListResponse(List<DetailedPosition> detailedPositions) {
        this.detailedPositionDtos = detailedPositions.stream().map(DetailedPositionDto::new).collect(Collectors.toList());
    }

    @Getter
    public static class DetailedPositionDto {
        private final Long id;
        private final String name;

        public DetailedPositionDto(DetailedPosition detailedPosition) {
            this.id = detailedPosition.getId();
            this.name = detailedPosition.getName();
        }
    }
}
