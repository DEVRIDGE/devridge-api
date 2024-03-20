package io.devridge.api.dto.roadmap;


import io.devridge.core.domain.roadmap.CourseDetail;
import io.devridge.core.domain.user.LoginStatus;
import io.devridge.core.domain.user.StudyStatus;
import lombok.Getter;

import java.util.*;
import java.util.stream.Collectors;

@Getter
public class CourseDetailResponseDto {
    private final String courseName;
    private final LoginStatus loginStatus;
    private final StudyStatus studyStatus;
    private final List<CourseDetailDto> courseDetails;

    public CourseDetailResponseDto(String title, UserStudyStatusDto userStudyStatusDto, List<CourseDetail> courseDetailList) {
        this.courseName = title;
        this.loginStatus = userStudyStatusDto.getLoginStatus();
        this.studyStatus = userStudyStatusDto.getStudyStatus();
        this.courseDetails = courseDetailList.stream()
                .map(CourseDetailDto::new)
                .collect(Collectors.toList());
    }

    @Getter
    public static class CourseDetailDto {
        private final Long id;
        private final String name;

        public CourseDetailDto(CourseDetail courseDetail) {
            this.id = courseDetail.getId();
            this.name = courseDetail.getName();
        }
    }
}
