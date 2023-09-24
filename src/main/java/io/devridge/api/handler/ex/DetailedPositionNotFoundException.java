package io.devridge.api.handler.ex;

public class DetailedPositionNotFoundException extends RuntimeException{
    public DetailedPositionNotFoundException() {
        super("해당 서비스 종류를 찾을 수 없습니다.");
    }
}
