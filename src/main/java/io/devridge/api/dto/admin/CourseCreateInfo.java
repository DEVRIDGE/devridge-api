package io.devridge.api.dto.admin;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CourseCreateInfo {

    @NotNull
    private Long jobId;

    @NotEmpty
    private String name;

    @NotEmpty
    private String type;

    @NotNull
    private Integer order;
}
