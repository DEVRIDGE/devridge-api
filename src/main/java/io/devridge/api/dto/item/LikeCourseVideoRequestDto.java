package io.devridge.api.dto.item;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class LikeCourseVideoRequestDto {
    @NotNull
    Long courseVideoId;

    public LikeCourseVideoRequestDto(Long courseVideoId) {
        this.courseVideoId = courseVideoId;
    }
}
