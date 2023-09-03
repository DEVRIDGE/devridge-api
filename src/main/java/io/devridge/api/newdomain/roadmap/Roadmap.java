package io.devridge.api.newdomain.roadmap;

import io.devridge.api.newdomain.BaseTimeEntity;
import io.devridge.api.newdomain.companyinfo.CompanyInfo;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Entity
public class Roadmap extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "roadmap_id")
    private Long id;

    @Column(name = "roadmap_order")
    private Integer order;

    @Enumerated(EnumType.STRING)
    @Column(name = "roadmap_matching_flag")
    private MatchingStatus matchingFlag;

    @JoinColumn(name = "course_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Course course;

    @JoinColumn(name = "company_info_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private CompanyInfo companyInfo;
}
