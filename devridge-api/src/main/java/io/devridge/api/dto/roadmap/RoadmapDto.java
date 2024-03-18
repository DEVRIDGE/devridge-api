package io.devridge.api.dto.roadmap;

import io.devridge.core.domain.recruitment.RecruitmentInfo;
import lombok.Builder;
import lombok.Getter;

import java.util.*;

@Getter
public class RoadmapDto {
    private final String companyName;
    private final String jobName;
    private final String recruitmentInfoUrl;
    private final List<CourseIndexList> courseList;

    // TODO. courseList 변수명 고민, RoadmapList가 더 나을 것 같음
    @Builder
    public RoadmapDto(RecruitmentInfo recruitmentInfo, List<CourseIndexList> roadmap) {
        this.companyName = recruitmentInfo.getCompany().getName();
        this.jobName = recruitmentInfo.getJob().getName();
        this.recruitmentInfoUrl = recruitmentInfo.getContent();
        this.courseList = roadmap;
    }
}
