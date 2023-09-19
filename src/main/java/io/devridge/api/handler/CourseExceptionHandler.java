package io.devridge.api.handler;

import io.devridge.api.dto.common.ApiResponse;
import io.devridge.api.handler.ex.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class CourseExceptionHandler {

    @ExceptionHandler(CompanyJobNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleCompanyJobNotFoundException(CompanyJobNotFoundException exception) {
        log.error("CompanyJobNotFoundException = {}", exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error(exception.getMessage()));
    }

    @ExceptionHandler(RoadmapNotMatchCourseAndCompanyInfoException.class)
    public ResponseEntity<ApiResponse<Object>> handleRoadmapNotMatchCourseAndCompanyInfoException(RoadmapNotMatchCourseAndCompanyInfoException exception) {
        log.error("RoadmapNotMatchCourseAndCompanyInfoException = {}", exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error("해당하는 코스가 없습니다."));
    }

    @ExceptionHandler(CourseDetailNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleCourseDetailNotFoundException(CourseDetailNotFoundException exception) {
        log.error("CourseDetailNotFoundException = {}", exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error(exception.getMessage()));
    }

    @ExceptionHandler(CompanyInfoNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleCompanyInfoNotFoundException(CompanyInfoNotFoundException exception) {
        log.error("CompanyInfoNotFoundExceptionException = {}", exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error(exception.getMessage()));
    }

    @ExceptionHandler(UnauthorizedCourseAccessException.class)
    public ResponseEntity<ApiResponse<Object>> handleUnauthorizedCourseAccessException(UnauthorizedCourseAccessException exception) {
        log.error("UnauthorizedCourseAccessException = {}", exception.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.error("Login required"));
    }
}
