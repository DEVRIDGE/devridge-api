package io.devridge.api.service.company;

import io.devridge.api.dto.company.JobListResponse;
import io.devridge.api.handler.ex.company.CompanyNotFoundException;
import io.devridge.api.repository.company.ApiCompanyJobRepository;
import io.devridge.core.domain.company.Company;
import io.devridge.core.domain.company.CompanyJob;
import io.devridge.core.domain.company.Job;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JobServiceTest {

    @InjectMocks
    private JobService jobService;

    @Mock
    private CompanyService companyService;

    @Mock
    private ApiCompanyJobRepository companyJobRepository;

    @DisplayName("회사 아이디로 직무 리스트 조회에 성공합니다.")
    @Test
    void getJobList_with_company_id_success_test() {
        // given
        Company company = Company.builder().id(1L).name("company1").build();
        Job job = Job.builder().id(1L).name("job1").build();
        Job job2 = Job.builder().id(2L).name("job2").build();
        CompanyJob companyJob = CompanyJob.builder().company(company).job(job).build();
        CompanyJob companyJob2 = CompanyJob.builder().company(company).job(job2).build();
        List<CompanyJob> companyJobs = new ArrayList<>();
        companyJobs.add(companyJob);
        companyJobs.add(companyJob2);

        // stub
        when(companyService.findCompany(1L)).thenReturn(java.util.Optional.of(company));
        when(companyJobRepository.getCompanyJobByCompany(company)).thenReturn(companyJobs);

        // when
        JobListResponse result = jobService.getJobListByCompanyId(1L);

        // then
        assertThat(result.getJobs().size()).isEqualTo(2);
        assertThat(result.getJobs().get(0).getId()).isEqualTo(1L);
        assertThat(result.getJobs().get(0).getName()).isEqualTo("job1");
        assertThat(result.getJobs().get(1).getId()).isEqualTo(2L);
        assertThat(result.getJobs().get(1).getName()).isEqualTo("job2");
    }

    @DisplayName("회사 아이디로 빈 직무 리스트 조회에 성공합니다.")
    @Test
    void getJobList_with_company_id_empty_test() {
        // given
        Company company = Company.builder().id(1L).name("company1").build();
        List<CompanyJob> companyJobs = new ArrayList<>();

        // stub
        when(companyService.findCompany(1L)).thenReturn(java.util.Optional.of(company));
        when(companyJobRepository.getCompanyJobByCompany(company)).thenReturn(companyJobs);

        // when
        JobListResponse result = jobService.getJobListByCompanyId(1L);

        // then
        assertThat(result.getJobs().size()).isEqualTo(0);
    }

    @DisplayName("회사를 찾을 수 없는 경우 예외를 던집니다.")
    @Test
    void getJobList_with_company_id_fail_test() {
        // stub
        when(companyService.findCompany(1L)).thenReturn(java.util.Optional.empty());

        // when
        assertThatThrownBy(() -> jobService.getJobListByCompanyId(1L))
                .isInstanceOf(CompanyNotFoundException.class);
    }

    @DisplayName("회사 아이디와 직무 아이디로 회사 직무를 찾는데 성공합니다.")
    @Test
    void findCompanyJob_success_test() {
        // given
        Company company = Company.builder().id(1L).name("company1").build();
        Job job = Job.builder().id(1L).name("job1").build();
        CompanyJob companyJob = CompanyJob.builder().company(company).job(job).build();

        // stub
        when(companyJobRepository.findByCompanyIdAndJobId(1L, 1L)).thenReturn(Optional.of(companyJob));

        // when
        Optional<CompanyJob> result = jobService.findCompanyJob(1L, 1L);

        // then
        assertThat(result.get().getCompany().getId()).isEqualTo(1L);
        assertThat(result.get().getJob().getId()).isEqualTo(1L);
    }

    @DisplayName("회사 아이디와 직무 아이디로 회사 직무를 찾는데 실패합니다.")
    @Test
    void findCompanyJob_fail_test() {
        // stub
        when(companyJobRepository.findByCompanyIdAndJobId(1L, 1L)).thenReturn(Optional.empty());

        // when
        Optional<CompanyJob> result = jobService.findCompanyJob(1L, 1L);

        // then
        assertThat(result).isEmpty();
    }
}