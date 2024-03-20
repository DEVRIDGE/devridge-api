package io.devridge.api.dto.roadmap;


import io.devridge.core.domain.roadmap.Course;
import io.devridge.core.domain.roadmap.MatchingFlag;
import io.devridge.core.domain.roadmap.Roadmap;
import io.devridge.core.domain.user.StudyStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
public class RoadmapAndStatusDto {
    private final MatchingFlag matchingFlag;
    private final Course course;
    private final StudyStatus studyStatus;

    @Builder
    public RoadmapAndStatusDto(MatchingFlag matchingFlag, Course course, StudyStatus studyStatus) {
        this.matchingFlag = matchingFlag;
        this.course = course;
        this.studyStatus = studyStatus;
    }
}
