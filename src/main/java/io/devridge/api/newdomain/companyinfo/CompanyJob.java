package io.devridge.api.newdomain.companyinfo;

import io.devridge.api.newdomain.BaseTimeEntity;
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
public class CompanyJob extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "company_job_id")
    private Long id;

    @JoinColumn(name = "job_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Job job;

    @JoinColumn(name = "company")
    @ManyToOne(fetch = FetchType.LAZY)
    private Company company;

    @Builder
    public CompanyJob(Long id, Company company, Job job) {
        this.id = id;
        this.company = company;
        this.job = job;
    }
}
