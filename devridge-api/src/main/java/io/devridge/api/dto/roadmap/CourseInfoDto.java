package io.devridge.api.dto.roadmap;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.devridge.core.domain.roadmap.CourseType;
import io.devridge.core.domain.roadmap.MatchingFlag;
import io.devridge.core.domain.user.StudyStatus;
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

    public CourseInfoDto(RoadmapAndStatusDto roadmapAndStatusDto) {
        this.id = roadmapAndStatusDto.getCourse().getId();
        this.name = roadmapAndStatusDto.getCourse().getName();
        this.type = roadmapAndStatusDto.getCourse().getType();
        this.matchingFlag = roadmapAndStatusDto.getMatchingFlag();
        this.order = roadmapAndStatusDto.getCourse().getOrder();
        this.studyStatus = roadmapAndStatusDto.getStudyStatus();
    }
}
