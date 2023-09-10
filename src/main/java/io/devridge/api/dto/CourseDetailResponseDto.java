package io.devridge.api.dto;

import io.devridge.api.domain.roadmap.CourseDetail;
import lombok.Getter;

import java.util.*;
import java.util.stream.Collectors;

@Getter
public class CourseDetailResponseDto {
    private String courseName;
    private List<CourseDetailDto> courseDetails;

    public CourseDetailResponseDto(String title, List<CourseDetail> courseDetailList) {
        this.courseName = title;
        this.courseDetails = courseDetailList.stream()
                .map(cd -> new CourseDetailDto(cd))
                .sorted(Comparator.comparing(CourseDetailDto::getName))
                .collect(Collectors.toList());  // 이름순 정렬
    }

    @Getter
    public class CourseDetailDto {
        private Long id;
        private String name;

        public CourseDetailDto(CourseDetail courseDetail) {
            this.id = courseDetail.getId();
            this.name = courseDetail.getName();
        }
    }

}
