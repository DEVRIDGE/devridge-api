package io.devridge.api.handler;

import io.devridge.api.dto.common.ApiResponse;
import io.devridge.api.handler.ex.*;
import io.devridge.api.handler.ex.company.CompanyJobNotFoundException;
import io.devridge.api.handler.ex.recruitment.RecruitmentInfoNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class CourseExceptionHandler {

    @ExceptionHandler(CourseNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleCourseNotFoundException(CourseNotFoundException exception) {
        log.error("CourseNotFoundException = {}", exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error(exception.getMessage()));
    }

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

    @ExceptionHandler(RecruitmentInfoNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleCompanyInfoNotFoundException(RecruitmentInfoNotFoundException exception) {
        log.error("CompanyInfoNotFoundExceptionException = {}", exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error(exception.getMessage()));
    }

    @ExceptionHandler(UnAuthorizedCourseAccessException.class)
    public ResponseEntity<ApiResponse<Object>> handleUnauthorizedCourseAccessException(UnAuthorizedCourseAccessException exception) {
        log.error("UnAuthorizedCourseAccessException = {}", exception.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.error("Login required"));
    }

    @ExceptionHandler(DeleteFailedExistVideoException.class)
    public ResponseEntity<ApiResponse<Object>> handleDeleteFailedExistVideoException(DeleteFailedExistVideoException exception) {
        log.error("DeleteFailedExistVideoException = {}", exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error(exception.getMessage()));
    }
}
