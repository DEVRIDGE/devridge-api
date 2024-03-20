package io.devridge.core.domain.education_materials.video;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CourseVideoModify {
    private final String title;
    private final String url;
    private final String owner;
    private final String thumbnail;
    private final VideoSource source;

    @Builder
    public CourseVideoModify(String title, String url, String owner, String thumbnail, VideoSource source) {
        this.title = title;
        this.url = url;
        this.owner = owner;
        this.thumbnail = thumbnail;
        this.source = source;
    }
}
