package io.devridge.api.dto;

import io.devridge.api.domain.companyinfo.Job;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class JobResponseDto {
    private List<JobDto> jobs;

    public JobResponseDto(List<Job> jobs) {
        this.jobs = jobs.stream().map(job -> new JobResponseDto.JobDto(job)).collect(Collectors.toList());
    }

    @Getter
    public class JobDto {
        private Long id;
        private String name;

        public JobDto(Job job) {
            this.id = job.getId();
            this.name = job.getName();
        }
    }
}
