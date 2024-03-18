package io.devridge.api.config.filter.exception;


import org.springframework.security.core.AuthenticationException;

public class JwtNotHaveIdAuthenticationException extends AuthenticationException {
    public JwtNotHaveIdAuthenticationException() {
        super("토큰 안에 유저 ID값이 없습니다.");
    }
}
