package io.devridge.api.dto.course;

import io.devridge.api.domain.course.Course;
import io.devridge.api.domain.course.CourseType;
import lombok.Getter;

import java.util.*;
import java.util.stream.Collectors;

@Getter
public class CourseListResponseDto {
    private List<List<CourseDto>> courseList;

    public CourseListResponseDto(List<Course> courseList) {
        Collection<List<CourseDto>> courseListCollection = courseList.stream()
                .map(CourseDto::new)
                .collect(Collectors.groupingBy(CourseDto::getTurn, TreeMap::new, Collectors.toList())).values();

        List<List<CourseDto>> courseListAddEmptyList = new ArrayList<>();
        CourseType previousCourseType = null;

        for (List<CourseDto> courseDto : courseListCollection) {
            if(previousCourseType == CourseType.SKILL && courseDto.get(0).getType().equals(CourseType.SKILL)) {
                courseListAddEmptyList.add(Collections.emptyList());
            }
            courseListAddEmptyList.add(courseDto);
            previousCourseType = courseDto.get(0).getType();
        }

        this.courseList = courseListAddEmptyList;
    }

    @Getter
    public static class CourseDto {
        private final Long id;
        private final String name;
        private final CourseType type;
        private final int turn;

        public CourseDto(Course course) {
            this.id = course.getId();
            this.name = course.getName();
            this.type = course.getType();
            this.turn = course.getTurn();
        }
    }
}
