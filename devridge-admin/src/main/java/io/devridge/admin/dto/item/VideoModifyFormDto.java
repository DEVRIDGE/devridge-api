package io.devridge.admin.dto.item;


import io.devridge.core.domain.education_materials.video.VideoSource;
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
    private VideoSource source;

    @Builder
    public VideoModifyFormDto(Long id, String title, String url, String owner, String thumbnail, VideoSource source) {
        this.id = id;
        this.title = title;
        this.url = url;
        this.owner = owner;
        this.thumbnail = thumbnail;
        this.source = source;
    }
}
