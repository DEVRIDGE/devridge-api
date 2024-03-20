package io.devridge.core.domain.education_materials.video;

import io.devridge.core.domain.common.BaseTimeEntity;
import io.devridge.core.domain.user.User;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Getter
@Builder
@AllArgsConstructor
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
}
