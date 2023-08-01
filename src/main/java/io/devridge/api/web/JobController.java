package io.devridge.api.web;

import io.devridge.api.dto.JobResponseDto;
import io.devridge.api.service.JobService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class JobController {
    private final JobService jobService;

    @GetMapping("/jobs")
    public JobResponseDto jobList() {
        return jobService.jobList();
    }
}
