package io.devridge.api.service;

import io.devridge.api.domain.companyinfo.*;
import io.devridge.api.domain.roadmap.CourseDetail;
import io.devridge.api.domain.video.CourseVideo;
import io.devridge.api.dto.companyinfo.DetailedPositionResponseDto;
import io.devridge.api.dto.course.CompanyJobInfo;
import io.devridge.api.handler.ex.CompanyJobNotFoundException;
import io.devridge.api.handler.ex.CourseDetailNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DetailedPositionServiceTest {
    @InjectMocks
    private DetailedPositionService detailedPositionService;
    @Mock
    private DetailedPositionRepository detailedPositionRepository;
    @Mock
    private CompanyJobRepository companyJobRepository;

    @DisplayName("회사와 직무가 주어지면 해당 detailedPostion 리스트를 정상적으로 반환한다")
    @Test
    public void getDetailedPositionList_success_test() {
        //given
        Company company = Company.builder().id(1L).name("토스").build();
        Job job = Job.builder().id(1L).name("백엔드").build();
        CompanyJob companyJob = CompanyJob.builder().id(1L).company(company).job(job).build();
        DetailedPosition detailedPosition1 = DetailedPosition.builder().id(1L).name("Product").company(company).build();
        DetailedPosition detailedPosition2 = DetailedPosition.builder().id(2L).name("Platform").company(company).build();

        JobDetailedPosition jobDetailedPosition1 = JobDetailedPosition.builder().id(1L).job(job).detailedPosition(detailedPosition1).build();
        JobDetailedPosition jobDetailedPosition2 = JobDetailedPosition.builder().id(2L).job(job).detailedPosition(detailedPosition2).build();

        List<DetailedPosition> detailedPositionList = makeDetailedPositionList(company);

        //stub
        when(companyJobRepository.findByCompanyIdAndJobId(company.getId(), job.getId())).thenReturn(Optional.of(companyJob));
        when(detailedPositionRepository.findByCompanyIdAndJobId(company.getId(), job.getId())).thenReturn(detailedPositionList);


        //when
        DetailedPositionResponseDto detailedPositionResponseDto = detailedPositionService.getDetailedPositionList(company.getId(), job.getId());

        //then
        assertThat(detailedPositionResponseDto.getDetailedPositionDtos().get(0).getId()).isEqualTo(1L);
        assertThat(detailedPositionResponseDto.getDetailedPositionDtos().get(1).getId()).isEqualTo(2L);
        assertThat(detailedPositionResponseDto.getDetailedPositionDtos().get(0).getName()).isEqualTo("Product");
        assertThat(detailedPositionResponseDto.getDetailedPositionDtos().get(1).getName()).isEqualTo("Platform");
        assertThat(detailedPositionResponseDto.getDetailedPositionDtos().get(0).getCompany()).isEqualTo(company);
        assertThat(detailedPositionResponseDto.getDetailedPositionDtos().get(1).getCompany()).isEqualTo(company);
    }

    @DisplayName("회사와 직무가 대응되지 않으면 에러가 발생한다")
    @Test
    public void getDetailedPositionList_fail_test() {
        //given
        long companyId = 100L;
        long job = 100L;

        //stub
        when(companyJobRepository.findByCompanyIdAndJobId(companyId, job)).thenReturn(Optional.empty());

        //when & then
        assertThatThrownBy(() -> detailedPositionService.getDetailedPositionList(companyId, job))
                .isInstanceOf(CompanyJobNotFoundException.class);
    }

    private List<DetailedPosition> makeDetailedPositionList(Company company) {
        List<DetailedPosition> detailedPositionList = new ArrayList<>();
        detailedPositionList.add(DetailedPosition.builder().id(1L).name("Product").company(company).build());
        detailedPositionList.add(DetailedPosition.builder().id(2L).name("Platform").company(company).build());
        return detailedPositionList;
    }
}
