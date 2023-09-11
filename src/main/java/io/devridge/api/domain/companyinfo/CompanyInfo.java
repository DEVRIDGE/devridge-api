package io.devridge.api.domain.companyinfo;

import io.devridge.api.domain.BaseTimeEntity;
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
public class CompanyInfo extends BaseTimeEntity {

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

    @Builder
    public CompanyInfo(Long id, String content, Job job, DetailedPosition detailedPosition, Company company) {
        this.id = id;
        this.content = content;
        this.job = job;
        this.detailedPosition = detailedPosition;
        this.company = company;
    }
}
