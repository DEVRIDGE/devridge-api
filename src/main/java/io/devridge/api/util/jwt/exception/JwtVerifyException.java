package io.devridge.api.util.jwt.exception;

public class JwtVerifyException extends RuntimeException {
    public JwtVerifyException(String message) {
        super(message);
    }
}
