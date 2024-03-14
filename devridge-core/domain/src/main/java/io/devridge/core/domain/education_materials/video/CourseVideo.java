package io.devridge.core.domain.education_materials.video;

import io.devridge.core.domain.common.BaseTimeEntity;
import io.devridge.core.domain.roadmap.CourseDetail;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Getter
@Builder
@AllArgsConstructor
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
}
