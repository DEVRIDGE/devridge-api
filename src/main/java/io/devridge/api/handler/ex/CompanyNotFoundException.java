package io.devridge.api.handler.ex;

public class CompanyNotFoundException extends RuntimeException{
    public CompanyNotFoundException() {
        super("해당 회사를 찾을 수 없습니다.");
    }
}
