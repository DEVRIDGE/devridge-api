package io.devridge.api.config.filter.exception;


import org.springframework.security.core.AuthenticationException;

public class UserNotFoundAuthenticationException extends AuthenticationException {
    public UserNotFoundAuthenticationException() {
        super("토큰에서 가져온 id 값에 해당하는 유저가 없습니다.");
    }
}
