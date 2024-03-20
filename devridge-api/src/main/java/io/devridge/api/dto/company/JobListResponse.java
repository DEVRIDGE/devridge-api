package io.devridge.api.dto.company;

import io.devridge.core.domain.company.CompanyJob;
import io.devridge.core.domain.company.Job;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class JobListResponse {

    private final List<JobDto> jobs;

    public JobListResponse(List<CompanyJob> companyJobs) {
        this.jobs = companyJobs.stream()
                .map(companyJob -> new JobDto(companyJob.getJob()))
                .collect(Collectors.toList());
    }

    @Getter
    public static class JobDto {
        private final Long id;
        private final String name;

        public JobDto(Job job) {
            this.id = job.getId();
            this.name = job.getName();
        }
    }
}
