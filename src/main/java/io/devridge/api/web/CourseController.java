package io.devridge.api.web;

import io.devridge.api.dto.CourseDetailResponseDto;
import io.devridge.api.dto.common.ApiResponse;
import io.devridge.api.dto.course.CourseListResponseDto;
import io.devridge.api.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @GetMapping("/courses")
    public ResponseEntity<ApiResponse<Object>> getCourseList(
            @RequestParam("company") long companyId,
            @RequestParam("job") long jobId,
            @RequestParam("detailedPosition") long detailPositionId) {

        CourseListResponseDto courseList = courseService.getCourseList(companyId, jobId, detailPositionId);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(courseList));
    }

    @GetMapping("/courses/{courseId}")
    public ResponseEntity<ApiResponse<Object>> courseDetailList(@PathVariable Long courseId,
                                                                @RequestParam("company") Long companyId,
                                                                @RequestParam("job") Long jobId,
                                                                @RequestParam("detailedPosition") Long detailedPositionList) {

        CourseDetailResponseDto courseDetailResponseDto = courseService.getCourseDetailList(courseId, companyId, jobId, detailedPositionList);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(courseDetailResponseDto));
    }
}
