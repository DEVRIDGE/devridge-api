package io.devridge.api.handler;

import io.devridge.api.dto.common.ApiResponse;
import io.devridge.api.handler.ex.NotHaveRefreshTokenException;
import io.devridge.api.util.jwt.exception.JwtExpiredException;
import io.devridge.api.util.jwt.exception.JwtVerifyException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class TokenExceptionHandler {

    @ExceptionHandler(NotHaveRefreshTokenException.class)
    public ResponseEntity<ApiResponse<Object>> handleNotHaveRefreshTokenException(NotHaveRefreshTokenException exception) {
        log.error("NotHaveRefreshTokenException = {}", exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error("토큰 오류"));
    }

    @ExceptionHandler(JwtVerifyException.class)
    public ResponseEntity<ApiResponse<Object>> handleJwtVerifyException(JwtVerifyException exception) {
        log.error("JwtVerifyException = {}", exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error("토큰 오류"));
    }

    @ExceptionHandler(JwtExpiredException.class)
    public ResponseEntity<ApiResponse<Object>> handleJwtExpiredException(JwtExpiredException exception) {
        log.error("JwtExpiredException = {}", exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error("토큰 만료"));
    }
}
