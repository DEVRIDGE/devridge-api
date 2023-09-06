package io.devridge.api.web;

import io.devridge.api.dto.CourseVideoResponseDto;
import io.devridge.api.dto.common.ApiResponse;
import io.devridge.api.service.CourseVideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CourseVideoController {

    private final CourseVideoService courseVideoService;

    @GetMapping("/videos/{courseDetailId}")
    public ResponseEntity<ApiResponse<Object>> courseVideoList(@PathVariable Long courseDetailId) {
        CourseVideoResponseDto courseVideoList = courseVideoService.getCourseVideoList(courseDetailId);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(courseVideoList));
    }
}
