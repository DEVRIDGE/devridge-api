package io.devridge.api.dto;

import io.devridge.api.domain.user.LoginStatus;
import io.devridge.api.domain.user.StudyStatus;
import io.devridge.api.dto.course.CourseDetailWithAbilityDto;
import io.devridge.api.dto.course.UserStudyStatusDto;
import lombok.Getter;

import java.util.*;
import java.util.stream.Collectors;

@Getter
public class CourseDetailResponseDto {
    private final String courseName;
    private final LoginStatus loginStatus;
    private final StudyStatus studyStatus;
    private final List<CourseDetailDto> courseDetails;

    public CourseDetailResponseDto(String title, UserStudyStatusDto userStudyStatusDto, List<CourseDetailWithAbilityDto> courseDetailList) {
        this.courseName = title;
        this.loginStatus = userStudyStatusDto.getLoginStatus();
        this.studyStatus = userStudyStatusDto.getStudyStatus();
        this.courseDetails = courseDetailList.stream()
                .map(CourseDetailDto::new)
                .collect(Collectors.toList());
    }

    @Getter
    public class CourseDetailDto {
        private final Long id;
        private final String name;

        public CourseDetailDto(CourseDetailWithAbilityDto courseDetailWithAbilityDto) {
            this.id = courseDetailWithAbilityDto.getCourseDetailId();
            this.name = courseDetailWithAbilityDto.getCourseDetailName();
        }
    }
}
