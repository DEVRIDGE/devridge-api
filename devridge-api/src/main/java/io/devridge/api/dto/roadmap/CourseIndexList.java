package io.devridge.api.dto.roadmap;

import lombok.Getter;

import java.util.List;

@Getter
public class CourseIndexList {
    private final int index;
    private final List<CourseInfoDto> courses;

    public CourseIndexList(int index, List<CourseInfoDto> courseInfoList) {
        this.index = index;
        this.courses = courseInfoList;
    }
}