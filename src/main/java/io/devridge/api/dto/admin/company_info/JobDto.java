package io.devridge.api.dto.admin.company_info;

import io.devridge.api.domain.companyinfo.Job;
import lombok.Getter;

@Getter
public class JobDto {
    private final Long id;
    private final String name;

    public JobDto(Job job) {
        this.id = job.getId();
        this.name = job.getName();
    }
}

