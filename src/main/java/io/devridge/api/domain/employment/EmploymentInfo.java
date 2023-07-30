package io.devridge.api.domain.employment;

import io.devridge.api.domain.BaseTimeEntity;
import io.devridge.api.domain.company_job.Company;
import io.devridge.api.domain.company_job.Job;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Entity
public class EmploymentInfo extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employment_info_id")
    private Long id;

    @Column(name = "employment_info_text")
    private String text;

    @Column(name = "employment_info_start_date")
    private LocalDate startDate;

    @JoinColumn(name = "company_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Company company;

    @JoinColumn(name = "job_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Job job;
}
