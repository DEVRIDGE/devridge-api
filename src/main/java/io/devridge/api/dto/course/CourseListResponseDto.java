package io.devridge.api.dto.course;

import io.devridge.api.domain.companyinfo.CompanyInfo;
import lombok.Getter;

import java.util.*;

@Getter
public class CourseListResponseDto {
    private final String companyName;
    private final String jobName;
    private final String companyInfoUrl;
    private final List<CourseIndexList> courseList;

    public CourseListResponseDto(CompanyInfo companyInfo, List<CourseIndexList> courseList) {
        this.companyName = companyInfo.getCompany().getName();
        this.jobName = companyInfo.getJob().getName();
        this.companyInfoUrl = companyInfo.getContent();
        this.courseList = courseList;
    }
}
