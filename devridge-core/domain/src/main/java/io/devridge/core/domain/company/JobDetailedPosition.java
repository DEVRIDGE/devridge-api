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
