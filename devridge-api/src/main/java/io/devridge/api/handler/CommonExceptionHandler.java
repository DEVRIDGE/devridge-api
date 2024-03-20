package io.devridge.api.handler;

import io.devridge.api.dto.common.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class CommonExceptionHandler {

    private static final String BAD_REQUEST_MESSAGE = "잘못된 형식 요청";
    private static final String NOT_FOUND_MESSAGE = "잘못된 주소 요청";
    private static final String UNSUPPORTED_MEDIA_TYPE = "지원하지 않는 형식";

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationException(HttpServletRequest request, BindingResult bindingResult) {
        log.error("request.getRequestURI() = {}, ", request.getRequestURI());
        log.error("bindingResult = {}", bindingResult);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error(bindingResult));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Object>> handleNotReadBodyException(HttpServletRequest request, HttpMessageNotReadableException exception) {
        log.error("request.getRequestURI() = {}, ", request.getRequestURI());
        log.error("HttpMessageNotReadableException = {}", exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error(BAD_REQUEST_MESSAGE));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponse<Object>> handleMissingRequestParamException(HttpServletRequest request, MissingServletRequestParameterException exception) {
        log.error("request.getRequestURI() = {}, ", request.getRequestURI());
        log.error("MissingServletRequestParameterException = {}", exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error(BAD_REQUEST_MESSAGE));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<Object>> handlePathVariableTypeMismatchException(HttpServletRequest request, MethodArgumentTypeMismatchException exception) {
        log.error("request.getRequestURI() = {}, ", request.getRequestURI());
        log.error("MethodArgumentTypeMismatchException = {}", exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error(BAD_REQUEST_MESSAGE));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse<Object>> handleWrongMethodException(HttpServletRequest request, HttpRequestMethodNotSupportedException exception) {
        log.error("request.getRequestURI() = {}, ", request.getRequestURI());
        log.error("HttpRequestMethodNotSupportedException = {}", exception.getMessage());
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(ApiResponse.error(NOT_FOUND_MESSAGE));
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ApiResponse<Object>> handleWrongMediaTypeException(HttpServletRequest request, HttpMediaTypeNotSupportedException exception) {
        log.error("request.getRequestURI() = {}, ", request.getRequestURI());
        log.error("HttpMediaTypeNotSupportedException = {}", exception.getMessage());
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(ApiResponse.error(UNSUPPORTED_MEDIA_TYPE));
    }
}
