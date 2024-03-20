package io.devridge.api.service.company;

import io.devridge.api.dto.company.DetailedPositionListResponse;
import io.devridge.api.handler.ex.company.CompanyJobNotFoundException;
import io.devridge.api.repository.company.ApiDetailedPositionRepository;
import io.devridge.core.domain.company.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DetailedPositionServiceTest {

    @InjectMocks
    private DetailedPositionService detailedPositionService;

    @Mock
    private JobService jobService;

    @Mock
    private ApiDetailedPositionRepository detailedPositionRepository;

    @DisplayName("상세 포지션 리스트 조회에 성공합니다.")
    @Test
    void getDetailedPositionList_success_test() {
        // given
        long companyId = 1L;
        long jobId = 1L;

        // stub
        Company company = Company.builder().id(1L).name("company1").build();
        List<DetailedPosition> detailedPositions = List.of(
                DetailedPosition.builder().id(1L).company(company).name("detailedPosition1").build(),
                DetailedPosition.builder().id(2L).company(company).name("detailedPosition2").build()
        );
        when(jobService.findCompanyJob(companyId, jobId)).thenReturn(Optional.of(CompanyJob.builder().build()));
        when(detailedPositionRepository.findByCompanyIdAndJobId(companyId, jobId)).thenReturn(detailedPositions);

        // when
        DetailedPositionListResponse detailedPositionList = detailedPositionService.getDetailedPositionList(companyId, jobId);

        // then
        assertThat(detailedPositionList.getDetailedPositionDtos().size()).isEqualTo(2);
        assertThat(detailedPositionList.getDetailedPositionDtos().get(0).getId()).isEqualTo(1L);
        assertThat(detailedPositionList.getDetailedPositionDtos().get(0).getName()).isEqualTo("detailedPosition1");
        assertThat(detailedPositionList.getDetailedPositionDtos().get(1).getId()).isEqualTo(2L);
        assertThat(detailedPositionList.getDetailedPositionDtos().get(1).getName()).isEqualTo("detailedPosition2");
    }

    @DisplayName("상세 포지션 빈 리스트 조회에 성공합니다.")
    @Test
    void getDetailedPositionList_empty_test() {
        // given
        long companyId = 1L;
        long jobId = 1L;

        // stub
        when(jobService.findCompanyJob(companyId, jobId)).thenReturn(Optional.of(CompanyJob.builder().build()));
        when(detailedPositionRepository.findByCompanyIdAndJobId(companyId, jobId)).thenReturn(List.of());

        // when
        DetailedPositionListResponse detailedPositionList = detailedPositionService.getDetailedPositionList(companyId, jobId);

        // then
        assertThat(detailedPositionList.getDetailedPositionDtos().size()).isEqualTo(0);
    }

    @DisplayName("회사 직무를 찾을 수 없으면 예외를 던집니다.")
    @Test
    void validateCompanyJob_notFound_test() {
        // given
        long companyId = 1L;
        long jobId = 1L;

        // stub
        when(jobService.findCompanyJob(companyId, jobId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> detailedPositionService.getDetailedPositionList(companyId, jobId))
                .isInstanceOf(CompanyJobNotFoundException.class);
    }
}