package io.devridge.api.web;

import io.devridge.api.dto.CourseDetailResponseDto;
import io.devridge.api.dto.common.ApiResponse;
import io.devridge.api.service.CourseDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RoadmapController {

    private final CourseDetailService courseDetailService;
    @PostMapping("/courses")
    public ResponseEntity<ApiResponse<Object>> courseDetailList(@RequestParam Long companyId,
                                                                @RequestParam Long jobId,
                                                                @RequestParam Long courseId) {
        CourseDetailResponseDto courseDetails = courseDetailService.getCourseDetails(courseId, companyId, jobId);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(courseDetails));
    }
}
