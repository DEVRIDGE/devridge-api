package io.devridge.api.handler.ex.recruitment;

public class RecruitmentInfoNotFoundException extends RuntimeException{
    public RecruitmentInfoNotFoundException() {
        super("채용 정보를 찾을 수 없습니다.");
    }
}
