package io.devridge.api.web;

import io.devridge.api.config.auth.LoginUser;
import io.devridge.api.dto.CourseDetailResponseDto;
import io.devridge.api.dto.common.ApiResponse;
import io.devridge.api.dto.course.ChangeStudyStatusRequestDto;
import io.devridge.api.dto.course.CourseListResponseDto;
import io.devridge.api.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @GetMapping("/courses")
    public ResponseEntity<ApiResponse<Object>> getCourseList(
            @RequestParam("company") Long companyId,
            @RequestParam("job") Long jobId,
            @RequestParam("detailedPosition") Long detailedPositionId,
            @AuthenticationPrincipal LoginUser loginUser) {

        CourseListResponseDto courseList = courseService.getCourseList(companyId, jobId, detailedPositionId, loginUser);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(courseList));
    }

    @GetMapping("/courses/{courseId}")
    public ResponseEntity<ApiResponse<Object>> courseDetailList(
            @PathVariable Long courseId,
            @RequestParam("company") Long companyId,
            @RequestParam("job") Long jobId,
            @RequestParam("detailedPosition") Long detailedPositionId,
            @AuthenticationPrincipal LoginUser loginUser) {

        CourseDetailResponseDto courseDetailResponseDto = courseService.getCourseDetailList(courseId, companyId, jobId, detailedPositionId, loginUser);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(courseDetailResponseDto));
    }

    @PostMapping("/courses/{courseId}/change-studystatus")
    public ResponseEntity<ApiResponse<Object>> changeStudyStatus(
            @PathVariable Long courseId,
            @Valid @RequestBody ChangeStudyStatusRequestDto changeStudyStatusRequestDto,
            @AuthenticationPrincipal LoginUser loginUser) {

        courseService.changeStudyStatus(courseId, changeStudyStatusRequestDto, loginUser);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("학습 상태를 성공적으로 변경하였습니다."));
    }
}
