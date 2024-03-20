package io.devridge.admin.handler.ex;

import lombok.Getter;

public class AdminRecruitmentInfoException extends RuntimeException {

    private AdminRecruitmentInfoException(String message) {
        super(message);
    }

    @Getter
    public static class AdminDuplicateRecruitmentInfo extends AdminRecruitmentInfoException {
        public AdminDuplicateRecruitmentInfo(Long recruitmentInfoId) {
            super(recruitmentInfoId + "에 해당 채용 정보가 이미 존재합니다.");
        }
    }
}
