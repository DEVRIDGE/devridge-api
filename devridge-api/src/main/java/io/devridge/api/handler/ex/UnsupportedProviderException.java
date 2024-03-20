package io.devridge.api.handler.ex;

public class UnsupportedProviderException extends RuntimeException {
    public UnsupportedProviderException(String provider) {
        super("지원하지 않는 소셜 로그인 방식입니다. provider : " + provider);
    }
}
