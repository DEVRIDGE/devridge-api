package io.devridge.api.web;

import io.devridge.api.dto.CourseDetailResponseDto;
import io.devridge.api.dto.common.ApiResponse;
import io.devridge.api.service.CourseDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CompanyDetailController {

    private final CourseDetailService courseDetailService;
    @GetMapping("/courses/{courseId}")
    public ResponseEntity<ApiResponse<Object>> courseDetailList(@PathVariable Long courseId,
                                                                @RequestParam Long companyId,
                                                                @RequestParam Long jobId) {
        CourseDetailResponseDto courseDetails = courseDetailService.getCourseDetails(courseId, companyId, jobId);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(courseDetails));
    }
}
