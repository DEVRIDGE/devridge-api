package io.devridge.admin.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CourseDetailCreateInfo {

    @NotNull
    private Long courseId;

    @NotEmpty
    private String name;

}
