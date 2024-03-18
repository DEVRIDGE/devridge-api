package io.devridge.api.service.recruitment;

import io.devridge.api.handler.ex.recruitment.RecruitmentInfoNotFoundException;
import io.devridge.api.repository.recruitment.ApiRecruitmentInfoRepository;
import io.devridge.core.domain.company.Company;
import io.devridge.core.domain.company.DetailedPosition;
import io.devridge.core.domain.company.Job;
import io.devridge.core.domain.recruitment.RecruitmentInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class RecruitmentInfoServiceTest {

    @InjectMocks
    private RecruitmentInfoService recruitmentInfoService;

    @Mock
    private ApiRecruitmentInfoRepository recruitmentInfoRepository;

    @DisplayName("채용 정보를 리턴합니다.")
    @Test
    void findRecruitmentInfo_success_test() {
        // given
        Long jobId = 1L;
        Long detailedPositionId = 1L;

        // stub
        Company company = Company.builder().id(1L).name("회사").build();
        Job job = Job.builder().id(2L).name("직무").build();
        DetailedPosition detailedPosition = DetailedPosition.builder().id(3L).name("상세 직무").company(company).build();

        when(recruitmentInfoRepository.findByRecruitmentInfoByJobIdAndDetailedPositionIdWithFetch(anyLong(), anyLong()))
                .thenReturn(Optional.of(RecruitmentInfo.builder().id(1L).content("채용 정보 내용").job(job).detailedPosition(detailedPosition).build()));

        // when
        Optional<RecruitmentInfo> result = recruitmentInfoService.findRecruitmentInfo(jobId, detailedPositionId);

        // then
        assertThat(result).isNotEmpty();
        assertThat(result.get().getId()).isEqualTo(1L);
        assertThat(result.get().getContent()).isEqualTo("채용 정보 내용");
        assertThat(result.get().getJob().getId()).isEqualTo(2L);
        assertThat(result.get().getJob().getName()).isEqualTo("직무");
        assertThat(result.get().getDetailedPosition().getId()).isEqualTo(3L);
        assertThat(result.get().getDetailedPosition().getName()).isEqualTo("상세 직무");
        assertThat(result.get().getDetailedPosition().getCompany().getId()).isEqualTo(1L);
        assertThat(result.get().getDetailedPosition().getCompany().getName()).isEqualTo("회사");
    }

    @DisplayName("채용 정보가 없으면 빈 Optional을 리턴합니다.")
    @Test
    void findRecruitmentInfo_not_found_test() {
        // given
        Long jobId = 1L;
        Long detailedPositionId = 1L;

        // stub
        when(recruitmentInfoRepository.findByRecruitmentInfoByJobIdAndDetailedPositionIdWithFetch(anyLong(), anyLong()))
                .thenReturn(Optional.empty());

        // when
        Optional<RecruitmentInfo> result = recruitmentInfoService.findRecruitmentInfo(jobId, detailedPositionId);

        // then
        assertThat(result).isEmpty();
    }

    @DisplayName("회사 정보를 검증하고 리턴합니다.")
    @Test
    void validateCompanyInfo_success_test() {
        // given
        Long companyId = 1L;
        Long jobId = 2L;
        Long detailedPositionId = 3L;

        // stub
        Company company = Company.builder().id(1L).name("회사").build();
        Job job = Job.builder().id(2L).name("직무").build();
        DetailedPosition detailedPosition = DetailedPosition.builder().id(3L).name("상세 직무").company(company).build();

        when(recruitmentInfoRepository.findByCompanyIdAndJobIdAndDetailedPositionId(anyLong(), anyLong(), anyLong()))
                .thenReturn(Optional.of(RecruitmentInfo.builder().id(1L).content("채용 정보 내용").job(job).detailedPosition(detailedPosition).build()));

        // when
        RecruitmentInfo result = recruitmentInfoService.validateCompanyInfo(companyId, jobId, detailedPositionId);

        // then
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getContent()).isEqualTo("채용 정보 내용");
        assertThat(result.getJob().getId()).isEqualTo(2L);
        assertThat(result.getJob().getName()).isEqualTo("직무");
        assertThat(result.getDetailedPosition().getId()).isEqualTo(3L);
        assertThat(result.getDetailedPosition().getName()).isEqualTo("상세 직무");
        assertThat(result.getDetailedPosition().getCompany().getId()).isEqualTo(1L);
        assertThat(result.getDetailedPosition().getCompany().getName()).isEqualTo("회사");
    }

    @DisplayName("회사 정보가 없으면 예외를 던집니다.")
    @Test
    void validateCompanyInfo_not_found_test() {
        // given
        Long companyId = 1L;
        Long jobId = 2L;
        Long detailedPositionId = 3L;

        // stub
        when(recruitmentInfoRepository.findByCompanyIdAndJobIdAndDetailedPositionId(anyLong(), anyLong(), anyLong()))
                .thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> recruitmentInfoService.validateCompanyInfo(companyId, jobId, detailedPositionId))
                .isInstanceOf(RecruitmentInfoNotFoundException.class);
    }
}