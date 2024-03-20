package io.devridge.api.dto.education_materials;

import io.devridge.core.domain.education_materials.video.VideoSource;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CourseVideoWithLikeDto {
    private final Long id;
    private final String title;
    private final String thumbnail;
    private final String url;
    private final VideoSource source;
    private final String userLikedYn;
    private final Long likeCnt;

    @Builder
    public CourseVideoWithLikeDto(Long id, String title, String thumbnail, String url, VideoSource source, Long courseVideoUserId, Long likeCnt) {
        this.id = id;
        this.title = title;
        this.thumbnail = thumbnail;
        this.url = url;
        this.source = source;
        this.userLikedYn = courseVideoUserId == null ? "NO" : "YES";
        this.likeCnt = likeCnt;
    }
}
