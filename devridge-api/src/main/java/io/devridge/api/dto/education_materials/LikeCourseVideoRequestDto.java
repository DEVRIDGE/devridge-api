package io.devridge.api.dto.education_materials;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class LikeCourseVideoRequestDto {
    @NotNull
    private Long courseVideoId;

    @Builder
    public LikeCourseVideoRequestDto(Long courseVideoId) {
        this.courseVideoId = courseVideoId;
    }
}
