package io.devridge.api.dto.companyinfo;

import io.devridge.api.domain.companyinfo.Company;
import io.devridge.api.domain.companyinfo.DetailedPosition;
import io.devridge.api.domain.companyinfo.Job;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class DetailedPositionResponseDto {
    private List<DetailedPositionDto> detailedPositionDtos;

    public DetailedPositionResponseDto(List<DetailedPosition> detailedPositions) {
        this.detailedPositionDtos = detailedPositions.stream().map(detailedPosition -> new DetailedPositionResponseDto.DetailedPositionDto(detailedPosition)).collect(Collectors.toList());
    }

    @Getter
    public class DetailedPositionDto {
        private Long id;
        private String name;
        private Company company;

        public DetailedPositionDto(DetailedPosition detailedPosition) {
            this.id = detailedPosition.getId();
            this.name = detailedPosition.getName();
            this.company = detailedPosition.getCompany();
        }
    }
}
