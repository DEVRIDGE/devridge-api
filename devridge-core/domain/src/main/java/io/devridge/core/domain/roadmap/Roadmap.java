package io.devridge.core.domain.roadmap;

import io.devridge.core.domain.common.BaseTimeEntity;
import io.devridge.core.domain.recruitment.RecruitmentInfo;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Entity
public class Roadmap extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "roadmap_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "roadmap_matching_flag")
    private MatchingFlag matchingFlag;

    @JoinColumn(name = "course_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Course course;

    @JoinColumn(name = "company_info_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private RecruitmentInfo recruitmentInfo;
}
