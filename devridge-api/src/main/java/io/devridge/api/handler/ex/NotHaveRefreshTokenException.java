package io.devridge.api.handler.ex;

public class NotHaveRefreshTokenException extends RuntimeException {
    public NotHaveRefreshTokenException() {
        super("DB에 refresh token이 없습니다.");
    }
}
