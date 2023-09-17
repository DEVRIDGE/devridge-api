package io.devridge.api.domain.roadmap;

import io.devridge.api.domain.BaseTimeEntity;
import io.devridge.api.domain.companyinfo.CompanyInfo;
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
public class Roadmap extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "roadmap_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "roadmap_matching_flag")
    private MatchingFlag matchingFlag;

    @JoinColumn(name = "course_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Course course;

    @JoinColumn(name = "company_info_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private CompanyInfo companyInfo;

    @Builder
    public Roadmap(Long id, MatchingFlag matchingFlag, Course course, CompanyInfo companyInfo) {
        this.id = id;
        this.matchingFlag = matchingFlag;
        this.course = course;
        this.companyInfo = companyInfo;
    }
}
