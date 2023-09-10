package io.devridge.api.handler;

import io.devridge.api.dto.common.ApiResponse;
import io.devridge.api.handler.ex.CompanyInfoNotFoundException;
import io.devridge.api.handler.ex.CompanyJobNotFoundException;
import io.devridge.api.handler.ex.CourseDetailNotFoundException;
import io.devridge.api.handler.ex.CourseNotFoundException;
import io.devridge.api.handler.ex.NotFoundCompanyInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class CourseExceptionHandler {

    @ExceptionHandler(NotFoundCompanyInfo.class)
    public ResponseEntity<ApiResponse<Object>> handleNotFoundCompanyInfo(NotFoundCompanyInfo exception) {
        log.error("NotFoundCompanyInfo = {}", exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error("해당하는 회사 정보가 없습니다."));
    }

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
}
