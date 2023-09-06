package io.devridge.api.web;

import io.devridge.api.dto.CourseDetailResponseDto;
import io.devridge.api.dto.CourseVideoResponseDto;
import io.devridge.api.dto.common.ApiResponse;
import io.devridge.api.dto.course.CourseListResponseDto;
import io.devridge.api.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @GetMapping("/courses")
    public ResponseEntity<ApiResponse<Object>> getCourseList(
            @RequestParam("company") long companyId,
            @RequestParam("job") long jobId
    ) {
        CourseListResponseDto courseList = courseService.getCourseList(companyId, jobId);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(courseList));
    }

    @GetMapping("/courses/{courseId}")
    public ResponseEntity<ApiResponse<Object>> courseDetailList(@PathVariable Long courseId,
                                                                @RequestParam("company") Long companyId,
                                                                @RequestParam("job") Long jobId) {
        CourseDetailResponseDto courseDetailList = courseService.getCourseDetailList(courseId, companyId, jobId);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(courseDetailList));
    }

    @GetMapping("/videos/{coursedetail}")
    public ResponseEntity<ApiResponse<Object>> courseVideoList(@PathVariable ("coursedetail") Long courseDetailId) {
        CourseVideoResponseDto courseVideoList = courseService.getCourseVideoList(courseDetailId);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(courseVideoList));
    }
}
