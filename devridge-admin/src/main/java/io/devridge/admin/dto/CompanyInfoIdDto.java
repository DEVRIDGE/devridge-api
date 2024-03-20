package io.devridge.admin.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class CompanyInfoIdDto {
    private Long companyInfoId;
    private Long companyId;
    private Long jobId;
    private Long detailedId;
    private Long roadmapId;

    public CompanyInfoIdDto(Long companyInfoId, Long companyId, Long jobId, Long detailedId, Long roadmapId) {
        this.companyInfoId = companyInfoId;
        this.companyId = companyId;
        this.jobId = jobId;
        this.detailedId = detailedId;
        this.roadmapId = roadmapId;
    }
}
