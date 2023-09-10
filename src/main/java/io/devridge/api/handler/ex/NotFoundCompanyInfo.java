package io.devridge.api.handler.ex;

public class NotFoundCompanyInfo extends RuntimeException {
    public NotFoundCompanyInfo() {
        super("회사 정보를 확인할 수 없습니다.");
    }
}
