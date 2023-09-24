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
public class JobDetailedPosition extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "job_detailed_position_id")
    private Long id;

    @JoinColumn(name = "job_id")
    /**
     * CompanyInfoService에서 JobDetailedPosition 저장할 때 부모객체가 자식객체 저장할 때
     * object references an unsaved transient instance - save the transient instance before flushing 에러
     * 발생할 수 있어서 cascade를 CascadeType.ALL로 바꿈
     */
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Job job;

    @JoinColumn(name = "detailed_position_id")
    /**
     * CompanyInfoService에서 JobDetailedPosition 저장할 때 부모객체가 자식객체 저장할 때
     * object references an unsaved transient instance - save the transient instance before flushing 에러
     * 발생할 수 있어서 cascade를 CascadeType.ALL로 바꿈
     */
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private DetailedPosition detailedPosition;

    @Builder
    public JobDetailedPosition(Long id, Job job, DetailedPosition detailedPosition) {
        this.id = id;
        this.job = job;
        this.detailedPosition = detailedPosition;
    }
}
