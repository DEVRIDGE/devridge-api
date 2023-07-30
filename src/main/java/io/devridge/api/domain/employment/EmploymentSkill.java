package io.devridge.api.domain.employment;

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
public class EmploymentSkill extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employment_skill_id")
    private Long id;

    @Column(name = "employment_skill_name")
    private String name;

    @JoinColumn(name = "employment_info_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private EmploymentInfo employmentInfo;
}
