package io.devridge.api.handler.ex.company;

public class CompanyJobNotFoundException extends RuntimeException{
    public CompanyJobNotFoundException() {
        super("회사와 직무에 일치하는 정보를 찾을 수 없습니다.");
    }
}
