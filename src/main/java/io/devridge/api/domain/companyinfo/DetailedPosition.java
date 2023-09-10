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
public class DetailedPosition extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "detailed_position_id")
    private Long id;

    @Column(name = "detailed_position_name")
    private String name;

    @JoinColumn(name = "company_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Company company;

    public DetailedPosition(String name, Company company) {
        this.name = name;
        this.company = company;
    }

    @Builder
    public DetailedPosition(Long id, String name, Company company) {
        this.id = id;
        this.name = name;
        this.company = company;
    }
}
