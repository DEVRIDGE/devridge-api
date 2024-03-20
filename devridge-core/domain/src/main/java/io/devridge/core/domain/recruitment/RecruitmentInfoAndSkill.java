package io.devridge.core.domain.recruitment;

import io.devridge.core.domain.common.BaseTimeEntity;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "company_info_company_required_ability")
@Entity
public class RecruitmentInfoAndSkill extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "company_info_company_required_ability_id")
    private Long id;

    @JoinColumn(name = "company_info_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private RecruitmentInfo recruitmentInfo;

    @JoinColumn(name = "company_required_ability_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private RecruitmentSkill recruitmentSkill;
}
