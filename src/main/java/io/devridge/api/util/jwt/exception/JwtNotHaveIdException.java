package io.devridge.api.util.jwt.exception;

public class JwtNotHaveIdException extends RuntimeException {
    public JwtNotHaveIdException() {
        super("JWT 토큰에 ID 값이 없습니다.");
    }
}
