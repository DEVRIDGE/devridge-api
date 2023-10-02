package io.devridge.api.web;

import io.devridge.api.dto.JobResponseDto;
import io.devridge.api.dto.common.ApiResponse;
import io.devridge.api.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class JobController {
    private final JobService jobService;

    @GetMapping("/jobs")
    public ResponseEntity<ApiResponse<Object>> jobListByCompanyId(@RequestParam("company") Long companyId) {
        JobResponseDto jobResponseDto = jobService.jobListByCompanyId(companyId);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(jobResponseDto));
    }
}
