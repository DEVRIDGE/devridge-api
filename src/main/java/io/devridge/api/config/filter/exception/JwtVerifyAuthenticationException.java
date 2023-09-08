package io.devridge.api.config.filter.exception;


import org.springframework.security.core.AuthenticationException;

public class JwtVerifyAuthenticationException extends AuthenticationException {
    public JwtVerifyAuthenticationException() {
        super("조작되었거나 다른 비밀키로 서명된 토큰입니다.");
    }
}
