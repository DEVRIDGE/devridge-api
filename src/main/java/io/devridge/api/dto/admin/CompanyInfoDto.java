package io.devridge.api.dto.admin;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class CompanyInfoDto {

    private String companyName;
    private String jobName;
    private String detailedPositionName;
    private Long roadmapId;

    public CompanyInfoDto(String companyName, String jobName, String detailedPositionName, Long roadmapId) {
        this.companyName = companyName;
        this.jobName = jobName;
        this.detailedPositionName = detailedPositionName;
        this.roadmapId = roadmapId;
    }
}
