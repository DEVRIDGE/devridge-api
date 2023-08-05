package io.devridge.api.domain.employment;

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
public class EmploymentSkillCourseDetail extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employment_skill_course_detail_id")
    private Long id;

    @JoinColumn(name = "employment_skill_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private EmploymentSkill employmentSkill;

    @JoinColumn(name = "course_detail_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private CourseDetail courseDetail;

    public EmploymentSkillCourseDetail(EmploymentSkill employmentSkill, CourseDetail courseDetail) {
        this.employmentSkill = employmentSkill;
        this.courseDetail = courseDetail;
    }
}
