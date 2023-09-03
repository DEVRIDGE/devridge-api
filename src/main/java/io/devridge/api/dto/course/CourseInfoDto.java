package io.devridge.api.dto.course;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.devridge.api.domain.course.Course;
import io.devridge.api.domain.course.CourseType;
import lombok.Getter;

@Getter
public class CourseInfoDto {
    private final Long id;
    private final String name;
    private final CourseType type;
    @JsonIgnore
    private final int turn;

    public CourseInfoDto(Course course) {
        this.id = course.getId();
        this.name = course.getName();
        this.type = course.getType();
        this.turn = course.getTurn();
    }
}
