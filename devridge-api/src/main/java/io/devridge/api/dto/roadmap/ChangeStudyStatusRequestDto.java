package io.devridge.api.dto.roadmap;

import io.devridge.core.domain.user.StudyStatus;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class ChangeStudyStatusRequestDto {
    @NotNull
    private Long companyId;
    @NotNull
    private Long jobId;
    @NotNull
    private Long detailedPositionId;
    @NotNull
    private StudyStatus studyStatus;

    public ChangeStudyStatusRequestDto(Long companyId, Long jobId, Long detailedPositionId, StudyStatus studyStatus) {
        this.companyId = companyId;
        this.jobId = jobId;
        this.detailedPositionId = detailedPositionId;
        this.studyStatus = studyStatus;
    }
}
