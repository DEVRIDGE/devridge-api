package io.devridge.api.domain.video;

import io.devridge.api.domain.BaseTimeEntity;
import io.devridge.api.domain.course.CourseDetail;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
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

    @Column(name = "course_video_name")
    private String name;

    @Column(name = "course_video_url")
    private String url;

    @Column(name = "course_video_thumbnail")
    private String thumbnail;

    @Enumerated(EnumType.STRING)
    @Column(name = "course_video_source")
    private VideoSource source;

    @JoinColumn(name = "course_detail_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private CourseDetail courseDetail;
}