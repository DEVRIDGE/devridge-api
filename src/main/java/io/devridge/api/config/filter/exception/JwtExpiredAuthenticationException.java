package io.devridge.api.config.filter.exception;


import org.springframework.security.core.AuthenticationException;

public class JwtExpiredAuthenticationException extends AuthenticationException {
    public JwtExpiredAuthenticationException() {
        super("만료된 토큰입니다.");
    }
}
