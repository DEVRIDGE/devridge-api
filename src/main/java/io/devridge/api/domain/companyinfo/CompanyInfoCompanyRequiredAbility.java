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
public class CompanyInfoCompanyRequiredAbility extends BaseTimeEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "company_info_company_required_ability_id")
    private Long id;

    @JoinColumn(name = "company_info_id")
    @ManyToOne(fetch = FetchType.LAZY)
    CompanyInfo companyInfo;

    @JoinColumn(name = "company_required_ability_id")
    @ManyToOne(fetch = FetchType.LAZY)
    CompanyRequiredAbility companyRequiredAbility;

    @Builder
    public CompanyInfoCompanyRequiredAbility(Long id, CompanyInfo companyInfo, CompanyRequiredAbility companyRequiredAbility) {
        this.id = id;
        this.companyInfo = companyInfo;
        this.companyRequiredAbility = companyRequiredAbility;
    }
}
