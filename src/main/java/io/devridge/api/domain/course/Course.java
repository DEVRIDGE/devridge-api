package io.devridge.api.domain.course;


import io.devridge.api.domain.BaseTimeEntity;
import io.devridge.api.domain.company_job.Job;
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

    @Column(name = "course_turn")
    private int turn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id")
    private Job job;

    public Course(String name, CourseType type, int turn, Job job) {
        this.name = name;
        this.type = type;
        this.turn = turn;
        this.job = job;
    }

    @Builder
    public Course(Long id, String name, CourseType type, int turn, Job job) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.turn = turn;
        this.job = job;
    }
}
