package io.devridge.api.domain.companyinfo;

import io.devridge.api.domain.BaseTimeEntity;
import io.devridge.api.domain.roadmap.CourseDetail;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Entity
public class CompanyRequiredAbility extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "company_required_ability_id")
    private Long id;

    @Column(name = "company_required_ability_name")
    private String name;

    @JoinColumn(name = "company_info_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private CompanyInfo companyInfo;

    @JoinColumn(name = "course_detail_id")
    @OneToOne(fetch = FetchType.LAZY)
    private CourseDetail courseDetail;

    public CompanyRequiredAbility(String name, CompanyInfo companyInfo, CourseDetail courseDetail) {
        this.name = name;
        this.companyInfo = companyInfo;
        this.courseDetail = courseDetail;
    }
}
