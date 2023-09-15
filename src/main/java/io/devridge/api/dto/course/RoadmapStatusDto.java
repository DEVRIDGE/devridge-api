package io.devridge.api.dto.course;

import io.devridge.api.domain.roadmap.Course;
import io.devridge.api.domain.roadmap.MatchingStatus;
import io.devridge.api.domain.user.StudyStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
public class RoadmapStatusDto {
    private final MatchingStatus matchingStatus;
    private final Course course;
    private final StudyStatus studyStatus;

    @Builder
    public RoadmapStatusDto(MatchingStatus matchingStatus, Course course, StudyStatus studyStatus) {
        this.matchingStatus = matchingStatus;
        this.course = course;
        this.studyStatus = studyStatus;
    }
}
