package io.devridge.api.dto.course;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CourseDetailWithAbilityDto {

    private Long courseDetailId;
    private String courseDetailName;
    private Long companyRequiredAbilityId;

    @Builder
    public CourseDetailWithAbilityDto(Long courseDetailId, String courseDetailName, Long companyRequiredAbilityId) {
        this.courseDetailId = courseDetailId;
        this.courseDetailName = courseDetailName;
        this.companyRequiredAbilityId = companyRequiredAbilityId;
    }
}
