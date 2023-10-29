package io.devridge.api.domain.companyinfo;

import io.devridge.api.config.JpaConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@Import(JpaConfig.class)
@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class CompanyInfoRepositoryTest {

    @Autowired
    private CompanyInfoRepository companyInfoRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private DetailedPositionRepository detailedPositionRepository;

    @Autowired
    private EntityManager em;

    @DisplayName("회사ID, 직무ID, 직무 상세ID를 가지고 회사 정보를 가져올 때 FETCH JOIN을 통해 회사와 직무 정보를 같이 가져온다")
    @Test
    public void findByCompanyIdAndJobIdAndDetailedPositionIdWithFetchJoin_test() {
        // given
        Company company = companyRepository.save(Company.builder().name("test company").build());
        Job job = jobRepository.save(Job.builder().name("test job").build());
        DetailedPosition detailedPosition = detailedPositionRepository.save(DetailedPosition.builder().name("test detail").company(company).build());
        companyInfoRepository.save(CompanyInfo.builder().job(job).detailedPosition(detailedPosition).company(company).build());
        em.flush();
        em.clear();

        // when
        CompanyInfo result = companyInfoRepository.findByCompanyIdAndJobIdAndDetailedPositionIdWithFetchJoin(company.getId(), job.getId(), detailedPosition.getId()).get();

        // then
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getCompany().getName()).isEqualTo("test company");
        assertThat(result.getJob().getName()).isEqualTo("test job");
    }

    @DisplayName("해당되는 회사정보가 없으면 빈 Optional을 반환한다")
    @Test
    public void findByCompanyIdAndJobIdAndDetailedPositionIdWithFetchJoin_optional_test() {
        // given & when
        Optional<CompanyInfo> result = companyInfoRepository.findByCompanyIdAndJobIdAndDetailedPositionIdWithFetchJoin(1L, 1L, 1L);

        // then
        assertThat(result).isEqualTo(Optional.empty());
    }
}