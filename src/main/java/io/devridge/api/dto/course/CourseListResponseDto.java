package io.devridge.api.dto.course;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.devridge.api.domain.course.Course;
import io.devridge.api.domain.course.CourseType;
import lombok.Getter;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Getter
public class CourseListResponseDto {
    private List<List<CourseDto>> courseList;

    public CourseListResponseDto(List<Course> courseList) {

        Collection<List<CourseDto>> courseListCollection = courseList.stream()
                .map((course) -> new CourseDto(course))
                .collect(Collectors.groupingBy(CourseDto::getTurn, TreeMap::new, Collectors.toList())).values();

        List<List<CourseDto>> courseListAddEmptyList = new ArrayList<>();
        CourseType previousCourseType = null;

        AtomicInteger indexCounter = new AtomicInteger(0);
        for (List<CourseDto> courseDtoList : courseListCollection) {

            if(previousCourseType == CourseType.SKILL && courseDtoList.get(0).getType().equals(CourseType.SKILL)) {
                courseListAddEmptyList.add(Collections.emptyList());
                indexCounter.getAndIncrement();
            }

            for (CourseDto courseDto : courseDtoList) {
                courseDto.setIndex(indexCounter.getAndIncrement());
            }
            courseListAddEmptyList.add(courseDtoList);
            previousCourseType = courseDtoList.get(0).getType();
        }

        this.courseList = courseListAddEmptyList;
    }

    @Getter
    public static class CourseDto {
        private int index;
        private final Long id;
        private final String name;
        private final CourseType type;
        @JsonIgnore
        private final int turn;

        public CourseDto(Course course) {
            this.id = course.getId();
            this.name = course.getName();
            this.type = course.getType();
            this.turn = course.getTurn();
        }

        public void setIndex(int index) {
            this.index = index;
        }
    }
}
