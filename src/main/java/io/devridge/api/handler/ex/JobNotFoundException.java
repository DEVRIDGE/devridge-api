package io.devridge.api.handler.ex;

public class JobNotFoundException extends RuntimeException{
    public JobNotFoundException() {
        super("해당 직무를 찾을 수 없습니다.");
    }
}
