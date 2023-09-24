package io.devridge.api.handler;

import io.devridge.api.dto.common.ApiResponse;
import io.devridge.api.handler.ex.CompanyJobNotFoundException;
import io.devridge.api.handler.ex.ExistingCompanyInfoException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class CompanyInfoExceptionHandler {
    @ExceptionHandler(ExistingCompanyInfoException.class)
    public ResponseEntity<ApiResponse<Object>> handleCompanyJobNotFoundException(ExistingCompanyInfoException exception) {
        log.error("ExistingCompanyInfoException = {}", exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error(exception.getMessage()));
    }
}
