package io.devridge.api.dto.course;

import lombok.Getter;

import java.util.*;

@Getter
public class CourseListResponseDto {
    private List<CourseIndexList> courseList;

    public CourseListResponseDto(List<CourseIndexList> courseList) {
        this.courseList = courseList;
    }
}
