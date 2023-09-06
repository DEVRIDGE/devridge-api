package io.devridge.api.domain.roadmap;

import io.devridge.api.domain.BaseTimeEntity;
import io.devridge.api.domain.companyinfo.Job;
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
public class Course extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id")
    private Long id;

    @Column(name = "course_name")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "course_type")
    private CourseType type;

    @Column(name = "course_order")
    private Integer order;

    @JoinColumn(name = "job_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Job job;

    @Builder
    public Course(Long id, String name, CourseType type, int order, Job job) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.order = order;
        this.job = job;
    }
}
