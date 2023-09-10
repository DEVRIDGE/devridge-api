package io.devridge.api.dto.course;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.devridge.api.domain.roadmap.CourseType;
import io.devridge.api.domain.roadmap.MatchingStatus;
import io.devridge.api.domain.roadmap.Roadmap;
import lombok.Getter;

@Getter
public class CourseInfoDto {
    private final Long id;
    private final String name;
    private final CourseType type;
    private final MatchingStatus matchingFlag;
    @JsonIgnore
    private final int order;

    public CourseInfoDto(Roadmap roadmap) {
        this.id = roadmap.getCourse().getId();
        this.name = roadmap.getCourse().getName();
        this.type = roadmap.getCourse().getType();
        this.matchingFlag = roadmap.getMatchingFlag();
        this.order = roadmap.getCourse().getOrder();
    }
}
