package io.devridge.api.handler.ex;

public class ExistingCompanyInfoException extends RuntimeException{
    public ExistingCompanyInfoException() {
        super("DB에 이미 존재하는 회사 정보입니다.");
    }
}
