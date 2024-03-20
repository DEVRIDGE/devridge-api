package io.devridge.admin.dto;


import io.devridge.core.domain.roadmap.Course;
import io.devridge.core.domain.roadmap.CourseType;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class CourseListDto {
    private List<CourseDto> courseDtoList;

    public CourseListDto(List<Course> courseList) {
        this.courseDtoList = courseList.stream()
                .map(CourseDto::new)
                .collect(Collectors.toList());
    }

    @Getter
    public class CourseDto {
        private Long id;
        private String name;
        private CourseType type;
        private Integer order;

        public CourseDto(Course course) {
            this.id = course.getId();
            this.name = course.getName();
            this.type = course.getType();
            this.order = course.getOrder();
        }
    }
}
