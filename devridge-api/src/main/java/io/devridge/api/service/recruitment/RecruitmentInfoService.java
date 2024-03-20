package io.devridge.api.service.recruitment;

import io.devridge.api.handler.ex.recruitment.RecruitmentInfoNotFoundException;
import io.devridge.api.repository.recruitment.ApiRecruitmentInfoRepository;
import io.devridge.core.domain.recruitment.RecruitmentInfo;
import lombok.*;

import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class RecruitmentInfoService {

    private final ApiRecruitmentInfoRepository recruitmentInfoRepository;

    public Optional<RecruitmentInfo> findRecruitmentInfo(Long jobId, Long detailedPositionId) {

        return recruitmentInfoRepository.findByRecruitmentInfoByJobIdAndDetailedPositionIdWithFetch(jobId, detailedPositionId);
    }

    // TODO. 메서드 이름 변경 필요 (고칠시 테스트도 함께 고칠것)
    public RecruitmentInfo validateCompanyInfo(Long companyId, Long jobId, Long detailedPositionId) {

        return recruitmentInfoRepository.findByCompanyIdAndJobIdAndDetailedPositionId(companyId, jobId, detailedPositionId)
                .orElseThrow(RecruitmentInfoNotFoundException::new);
    }
}