package io.devridge.api.web;

import io.devridge.api.dto.common.ApiResponse;
import io.devridge.api.dto.course.CourseListResponseDto;
import io.devridge.api.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
}
