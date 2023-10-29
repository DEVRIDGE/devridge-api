package io.devridge.api.domain.video;

import io.devridge.api.domain.BaseTimeEntity;
import io.devridge.api.domain.user.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Entity
public class CourseVideoUser extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_video_user_id")
    private Long id;

    @JoinColumn(name = "course_video_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private CourseVideo courseVideo;

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Builder
    public CourseVideoUser(Long id, CourseVideo courseVideo, User user) {
        this.id = id;
        this.courseVideo = courseVideo;
        this.user = user;
    }
}
