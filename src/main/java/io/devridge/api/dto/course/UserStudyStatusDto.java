package io.devridge.api.dto.course;

import io.devridge.api.domain.user.LoginStatus;
import io.devridge.api.domain.user.StudyStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserStudyStatusDto {
    private LoginStatus loginStatus;
    private StudyStatus studyStatus;

    @Builder
    public UserStudyStatusDto(LoginStatus loginStatus, StudyStatus studyStatus) {
        this.loginStatus = loginStatus;
        this.studyStatus = studyStatus;
    }
}
