package io.devridge.api.dto.course;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.devridge.api.domain.roadmap.CourseType;
import io.devridge.api.domain.roadmap.MatchingFlag;
import io.devridge.api.domain.user.StudyStatus;
import lombok.Getter;

@Getter
public class CourseInfoDto {
    private final Long id;
    private final String name;
    private final CourseType type;
    private final MatchingFlag matchingFlag;
    private final StudyStatus studyStatus;
    @JsonIgnore
    private final int order;

    public CourseInfoDto(RoadmapStatusDto roadmapStatusDto) {
        this.id = roadmapStatusDto.getCourse().getId();
        this.name = roadmapStatusDto.getCourse().getName();
        this.type = roadmapStatusDto.getCourse().getType();
        this.matchingFlag = roadmapStatusDto.getMatchingFlag();
        this.order = roadmapStatusDto.getCourse().getOrder();
        this.studyStatus = roadmapStatusDto.getStudyStatus();
    }
}
