package io.devridge.api.handler;

import io.devridge.api.dto.common.ApiResponse;
import io.devridge.api.handler.ex.NotFoundCourseVideoException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class CourseVidoeExceptionHandler {
    @ExceptionHandler(NotFoundCourseVideoException.class)
    public ResponseEntity<ApiResponse<Object>> handleNotFoundCourseVideoExceptionn(NotFoundCourseVideoException exception) {
        log.error("NotFoundCourseVideoException = {}", exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error(exception.getMessage()));
    }
}
