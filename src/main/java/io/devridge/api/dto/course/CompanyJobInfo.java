package io.devridge.api.dto.course;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CompanyJobInfo {
    private final String companyName;
    private final String companyLogo;
    private final String jobName;

    @Builder
    public CompanyJobInfo(String companyName, String companyLogo, String jobName) {
        this.companyName = companyName;
        this.companyLogo = companyLogo;
        this.jobName = jobName;
    }
}
