package io.devridge.admin.dto.course;

import io.devridge.core.domain.roadmap.CourseDetail;
import lombok.Getter;

@Getter
public class CourseDetailDto {
    private final Long id;
    private final String name;
    private final String description;

    public CourseDetailDto(CourseDetail courseDetail) {
        this.id = courseDetail.getId();
        this.name = courseDetail.getName();
        this.description = courseDetail.getDescription();
    }
}
