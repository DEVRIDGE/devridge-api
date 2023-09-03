//package io.devridge.api.newdomain.video;
//
//import io.devridge.api.newdomain.BaseTimeEntity;
//import io.devridge.api.newdomain.roadmap.CourseDetail;
//import lombok.AccessLevel;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import org.springframework.data.jpa.domain.support.AuditingEntityListener;
//
//import javax.persistence.*;
//
//@Getter
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
//@EntityListeners(AuditingEntityListener.class)
//@Entity
//public class CourseVideo extends BaseTimeEntity {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "course_video_id")
//    private Long id;
//
//    @Column(name = "course_video_title")
//    private String title;
//
//    @Column(name = "course_video_url")
//    private String url;
//
//    @Column(name = "course_video_thumbnail")
//    private String thumbnail;
//
//    @Column(name = "course_video_like_cnt")
//    private Integer likeCnt;
//
//    @Enumerated(EnumType.STRING)
//    @Column(name = "course_video_source")
//    private VideoSource source;
//
//    @JoinColumn(name = "course_detail_id")
//    @ManyToOne(fetch = FetchType.LAZY)
//    private CourseDetail courseDetail;
//
//    public CourseVideo(String title, String url, String thumbnail, Integer likeCnt, VideoSource source, CourseDetail courseDetail) {
//        this.title = title;
//        this.url = url;
//        this.thumbnail = thumbnail;
//        this.likeCnt = likeCnt;
//        this.source = source;
//        this.courseDetail = courseDetail;
//    }
//}
