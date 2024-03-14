package io.devridge.core.domain.company;

import io.devridge.core.domain.common.BaseTimeEntity;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Entity
public class CompanyJob extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "company_job_id")
    private Long id;

    /**
     * CompanyInfoService에서 CompanyJob 저장할 때 부모객체가 자식객체 저장할 때
     * object references an unsaved transient instance - save the transient instance before flushing 에러
     * 발생할 수 있어서 cascade를 CascadeType.ALL로 바꿈
     */
    @JoinColumn(name = "job_id")
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Job job;

    /**
     * CompanyInfoService에서 CompanyJob 저장할 때 부모객체가 자식객체 저장할 때
     * object references an unsaved transient instance - save the transient instance before flushing 에러
     * 발생할 수 있어서 cascade를 CascadeType.ALL로 바꿈
     */
    @JoinColumn(name = "company_id")
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Company company;
}
