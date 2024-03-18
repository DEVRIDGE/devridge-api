package io.devridge.api.dto.education_materials;

import io.devridge.core.domain.education_materials.video.VideoSource;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CourseVideoWithLikeDto {
    private Long id;
    private String title;
    private String thumbnail;
    private String url;
    private VideoSource source;
    private String userLikedYn;
    private Long likeCnt;

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
