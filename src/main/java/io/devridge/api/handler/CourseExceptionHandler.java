package io.devridge.api.handler;

import io.devridge.api.dto.common.ApiResponse;
import io.devridge.api.handler.ex.CompanyJobNotFoundException;
import io.devridge.api.handler.ex.CourseNotFoundException;
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

    @ExceptionHandler(CourseNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleCourseNotFoundException(CourseNotFoundException exception) {
        log.error("CourseNotFoundException = {}", exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error(exception.getMessage()));
    }
}
