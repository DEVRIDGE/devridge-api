package io.devridge.api.web;

import io.devridge.api.dto.company.CompanyListResponse;
import io.devridge.api.dto.common.ApiResponse;
import io.devridge.api.dto.company.JobListResponse;
import io.devridge.api.dto.company.DetailedPositionListResponse;
import io.devridge.api.service.company.CompanyService;
import io.devridge.api.service.company.DetailedPositionService;
import io.devridge.api.service.company.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;
    private final JobService jobService;
    private final DetailedPositionService detailedPositionService;

    @GetMapping("/companies")
    public ResponseEntity<ApiResponse<CompanyListResponse>> getCompanies() {
        CompanyListResponse companyListResponse = companyService.getCompanyList();

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(companyListResponse));
    }

    @GetMapping("/companies/{companyId}/jobs")
    public ResponseEntity<ApiResponse<JobListResponse>> getJobs(@PathVariable("companyId") Long companyId) {
        JobListResponse jobListResponse = jobService.getJobListByCompanyId(companyId);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(jobListResponse));
    }

    @GetMapping("/companies/{companyId}/jobs/{jobId}/detailedPositions")
    public ResponseEntity<ApiResponse<Object>> getDetailedPositions(@PathVariable("companyId") Long companyId, @PathVariable("jobId") Long jobId){
        DetailedPositionListResponse detailedPositionListResponse = detailedPositionService.getDetailedPositionList(companyId, jobId);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(detailedPositionListResponse));
    }
}
