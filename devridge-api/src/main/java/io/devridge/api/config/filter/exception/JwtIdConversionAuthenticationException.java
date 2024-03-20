package io.devridge.api.config.filter.exception;


import org.springframework.security.core.AuthenticationException;

public class JwtIdConversionAuthenticationException extends AuthenticationException {
    public JwtIdConversionAuthenticationException() {
        super("유저 ID값을 long값으로 변환할 수 없습니다.");
    }
}
