package io.devridge.api.dto.course;

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
    private StudyStatusDto studyStatus;

    public ChangeStudyStatusRequestDto(Long companyId, Long jobId, Long detailedPositionId, StudyStatusDto studyStatusDto) {
        this.companyId = companyId;
        this.jobId = jobId;
        this.detailedPositionId = detailedPositionId;
        this.studyStatus = studyStatusDto;
    }
}
