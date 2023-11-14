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

    @Column(name = "course_detail_description")
    private String description;

    @Builder
    public CourseDetail(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }
}
