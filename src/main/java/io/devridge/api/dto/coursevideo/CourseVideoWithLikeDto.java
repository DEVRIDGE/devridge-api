package io.devridge.api.dto.coursevideo;

import io.devridge.api.domain.video.CourseVideo;
import io.devridge.api.domain.video.VideoSource;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CourseVideoWithLikeDto {
    private Long id;
    private String title;
    private String thumbnail;
    private String url;
    private VideoSource source;
    private Long likeCnt;

    @Builder
    public CourseVideoWithLikeDto(Long id, String title, String thumbnail, String url, VideoSource source, Long likeCnt) {
        this.id = id;
        this.title = title;
        this.thumbnail = thumbnail;
        this.url = url;
        this.source = source;
        this.likeCnt = likeCnt;
    }
}
