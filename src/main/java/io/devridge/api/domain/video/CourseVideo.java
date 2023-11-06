package io.devridge.api.domain.video;

import io.devridge.api.domain.BaseTimeEntity;
import io.devridge.api.domain.roadmap.CourseDetail;
import io.devridge.api.dto.admin.item.VideoModifyFormDto;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Entity
public class CourseVideo extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_video_id")
    private Long id;

    @Column(name = "course_video_title")
    private String title;

    @Column(name = "course_video_url")
    private String url;

    @Column(name = "course_video_owner")
    private String owner;

    @Column(name = "course_video_thumbnail")
    private String thumbnail;

    @Enumerated(EnumType.STRING)
    @Column(name = "course_video_source")
    private VideoSource source;

    @Enumerated(EnumType.STRING)
    @Column(name = "course_video_language")
    private VideoLanguage language;

    @JoinColumn(name = "course_detail_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private CourseDetail courseDetail;

    @Builder
    public CourseVideo(Long id, String title, String url, String owner, String thumbnail, VideoSource source, VideoLanguage language, CourseDetail courseDetail) {
        this.id = id;
        this.title = title;
        this.url = url;
        this.owner = owner;
        this.thumbnail = thumbnail;
        this.source = source;
        this.language = language;
        this.courseDetail = courseDetail;
    }

    public void modifyVideoInfo(VideoModifyFormDto videoModifyFormDto) {
        this.title = videoModifyFormDto.getTitle();
        this.url = videoModifyFormDto.getUrl();
        this.owner = videoModifyFormDto.getOwner();
        this.thumbnail = videoModifyFormDto.getThumbnail();
        this.source = videoModifyFormDto.getType();
    }
}
