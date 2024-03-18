package io.devridge.api.web;

import io.devridge.api.dto.company.CompanyListDto;
import io.devridge.api.dto.common.ApiResponse;
import io.devridge.api.dto.company.JobListDto;
import io.devridge.api.dto.company.DetailedPositionListDto;
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
    public ResponseEntity<ApiResponse<CompanyListDto>> getCompanies() {
        CompanyListDto companyListDto = companyService.getCompanyList();

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(companyListDto));
    }

    @GetMapping("/companies/{companyId}/jobs")
    public ResponseEntity<ApiResponse<JobListDto>> getJobs(@PathVariable("company") Long companyId) {
        JobListDto jobListDto = jobService.getJobListByCompanyId(companyId);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(jobListDto));
    }

    @GetMapping("/companies/{companyId}/jobs/{jobId}/detailedPositions")
    public ResponseEntity<ApiResponse<Object>> getDetailedPositions(@PathVariable("companyId") Long companyId, @PathVariable("jobId") Long jobId){
        DetailedPositionListDto detailedPositionListDto = detailedPositionService.getDetailedPositionList(companyId, jobId);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(detailedPositionListDto));
    }
}
