package io.devridge.api.dto.course;

import io.devridge.api.domain.roadmap.Course;
import io.devridge.api.domain.roadmap.MatchingFlag;
import io.devridge.api.domain.user.StudyStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
public class RoadmapStatusDto {
    private final MatchingFlag matchingFlag;
    private final Course course;
    private final StudyStatus studyStatus;

    @Builder
    public RoadmapStatusDto(MatchingFlag matchingFlag, Course course, StudyStatus studyStatus) {
        this.matchingFlag = matchingFlag;
        this.course = course;
        this.studyStatus = studyStatus;
    }
}
