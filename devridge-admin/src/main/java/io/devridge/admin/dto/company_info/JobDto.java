package io.devridge.admin.dto.company_info;

import io.devridge.core.domain.company.Job;
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

