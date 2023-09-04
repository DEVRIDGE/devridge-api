package io.devridge.api.dto.course;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CompanyJobInfo {
    private final String companyName;
    private final String jobName;

    @Builder
    public CompanyJobInfo(String companyName, String jobName) {
        this.companyName = companyName;
        this.jobName = jobName;
    }
}
