package io.devridge.api.config.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.devridge.api.config.filter.exception.*;
import io.devridge.api.dto.common.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        log.error("request.getRequestURI = {}", request.getRequestURI());
        log.error("AuthenticationException = {}", authException.getMessage());
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json; charset=utf-8");
        ApiResponse<Object> apiResponse = ApiResponse.error(getErrorMessage(authException));
        String responseBody = objectMapper.writeValueAsString(apiResponse);
        response.getWriter().write(responseBody);
    }

    private String getErrorMessage(AuthenticationException authException) {
        if (authException instanceof JwtVerifyAuthenticationException ||
            authException instanceof JwtIdConversionAuthenticationException ||
            authException instanceof JwtNotHaveIdAuthenticationException ||
            authException instanceof UserNotFoundAuthenticationException) {
            return "토큰 검증 오류";
        } else if (authException instanceof JwtExpiredAuthenticationException) {
            return "토큰 만료";
        } else {
            return "로그인 필요";
        }
    }
}