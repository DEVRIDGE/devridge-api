package io.devridge.api.domain.companyinfo;

import io.devridge.api.domain.BaseTimeEntity;
import lombok.AccessLevel;
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
    @ManyToOne(fetch = FetchType.LAZY)
    private Job job;

    @JoinColumn(name = "detailed_position_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private DetailedPosition detailedPosition;
}
