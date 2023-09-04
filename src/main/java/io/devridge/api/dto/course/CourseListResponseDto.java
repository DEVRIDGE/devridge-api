package io.devridge.api.dto.course;

import lombok.Getter;

import java.util.*;

@Getter
public class CourseListResponseDto {
    private final String companyName;
    private final String jobName;
    private final List<CourseIndexList> courseList;

    public CourseListResponseDto(CompanyJobInfo companyJobInfo, List<CourseIndexList> courseList) {
        this.companyName = companyJobInfo.getCompanyName();
        this.jobName = companyJobInfo.getJobName();
        this.courseList = courseList;
    }
}
