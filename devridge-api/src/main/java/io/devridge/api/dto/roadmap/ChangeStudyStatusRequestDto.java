package io.devridge.api.dto.roadmap;

import io.devridge.core.domain.user.StudyStatus;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class ChangeStudyStatusRequestDto {
    @NotNull
    private final Long companyId;
    @NotNull
    private final Long jobId;
    @NotNull
    private final Long detailedPositionId;
    @NotNull
    private final StudyStatus studyStatus;

    public ChangeStudyStatusRequestDto(Long companyId, Long jobId, Long detailedPositionId, StudyStatus studyStatus) {
        this.companyId = companyId;
        this.jobId = jobId;
        this.detailedPositionId = detailedPositionId;
        this.studyStatus = studyStatus;
    }
}
