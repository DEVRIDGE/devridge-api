package io.devridge.admin.dto.item;

import io.devridge.core.domain.education_materials.video.CourseVideo;
import io.devridge.core.domain.education_materials.video.VideoSource;
import io.devridge.core.domain.roadmap.CourseDetail;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
public class VideoRegisterFormDto {
    private String title;
    private String url;
    private String owner;
    @Setter
    private String thumbnail;
    private VideoSource type;

    @Builder
    public VideoRegisterFormDto(String title, String url, String owner, String thumbnail, VideoSource type) {
        this.title = title;
        this.url = url;
        this.owner = owner;
        this.thumbnail = thumbnail;
        this.type = type;
    }

    public CourseVideo toDomain(CourseDetail courseDetail) {
        return CourseVideo.builder()
                .title(this.getTitle())
                .url(this.getUrl())
                .owner(this.getOwner())
                .thumbnail(this.getThumbnail())
                .source(this.getType())
                .courseDetail(courseDetail)
                .build();
    }
}
