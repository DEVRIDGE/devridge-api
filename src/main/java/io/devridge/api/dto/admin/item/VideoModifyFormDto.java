package io.devridge.api.dto.admin.item;

import io.devridge.api.domain.video.VideoSource;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
public class VideoModifyFormDto {
    private Long id;
    private String title;
    private String url;
    private String owner;
    @Setter
    private String thumbnail;
    private VideoSource type;

    @Builder
    public VideoModifyFormDto(Long id, String title, String url, String owner, String thumbnail, VideoSource type) {
        this.id = id;
        this.title = title;
        this.url = url;
        this.owner = owner;
        this.thumbnail = thumbnail;
        this.type = type;
    }
}
