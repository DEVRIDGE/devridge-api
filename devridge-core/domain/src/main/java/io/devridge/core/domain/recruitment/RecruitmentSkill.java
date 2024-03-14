package io.devridge.core.domain.recruitment;

import io.devridge.core.domain.common.BaseTimeEntity;
import io.devridge.core.domain.roadmap.CourseDetail;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "company_required_ability")
@Entity
public class RecruitmentSkill extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "company_required_ability_id")
    private Long id;

    @Column(name = "company_required_ability_name")
    private String name;

    @JoinColumn(name = "course_detail_id")
    @OneToOne(fetch = FetchType.LAZY)
    private CourseDetail courseDetail;
}
