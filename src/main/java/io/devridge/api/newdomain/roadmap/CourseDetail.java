package io.devridge.api.newdomain.roadmap;

import io.devridge.api.newdomain.BaseTimeEntity;
import io.devridge.api.newdomain.companyinfo.Job;
import lombok.AccessLevel;
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

    @JoinColumn(name = "course_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Course course;

}
