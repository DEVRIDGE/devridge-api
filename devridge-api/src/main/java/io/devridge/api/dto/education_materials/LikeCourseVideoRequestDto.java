package io.devridge.api.dto.education_materials;

import lombok.Builder;
import lombok.Getter;
import javax.validation.constraints.NotNull;

@Getter
public class LikeCourseVideoRequestDto {
    @NotNull
    private final Long courseVideoId;

    @Builder
    public LikeCourseVideoRequestDto(Long courseVideoId) {
        this.courseVideoId = courseVideoId;
    }
}
