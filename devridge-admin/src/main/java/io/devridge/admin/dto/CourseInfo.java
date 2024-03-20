package io.devridge.admin.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CourseInfo {

    @NotNull
    private Long id;
    @NotEmpty
    private String name;
    @NotEmpty
    private String type;
    @NotNull
    private Integer order;
}
