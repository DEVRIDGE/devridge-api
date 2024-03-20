package io.devridge.api.handler.ex;

public class UnmatchedEmailAndProviderException extends RuntimeException {
    public UnmatchedEmailAndProviderException() {
        super("이메일과 소셜 로그인 정보가 일치하지 않습니다.");
    }
}
