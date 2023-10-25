package io.devridge.api.domain.roadmap;

import io.devridge.api.domain.BaseTimeEntity;
import io.devridge.api.dto.admin.CourseDetailInfo;
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
public class CourseDetail extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_detail_id")
    private Long id;

    @Column(name = "course_detail_name")
    private String name;

    // 코스
    @JoinColumn(name = "course_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Course course;

    @Builder
    public CourseDetail(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public void changeCourseDetailInfo(CourseDetailInfo courseDetailInfo) {
        this.name = courseDetailInfo.getName();
    }
}
