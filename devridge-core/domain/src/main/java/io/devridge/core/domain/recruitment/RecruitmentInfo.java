package io.devridge.core.domain.recruitment;

import io.devridge.core.domain.common.BaseTimeEntity;
import io.devridge.core.domain.company.Company;
import io.devridge.core.domain.company.DetailedPosition;
import io.devridge.core.domain.company.Job;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "company_info")
@Entity
public class RecruitmentInfo extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "company_info_id")
    private Long id;

    @Column(name = "company_info_content")
    private String content;

    @JoinColumn(name = "job_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Job job;

    @JoinColumn(name = "detailed_position_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private DetailedPosition detailedPosition;

    @JoinColumn(name = "company_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Company company;
}
