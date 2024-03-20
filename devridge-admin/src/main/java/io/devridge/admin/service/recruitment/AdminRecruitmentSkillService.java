package io.devridge.admin.service.recruitment;

import io.devridge.admin.dto.recruitment.NotMatchedRequiredSkillsDto;
import io.devridge.admin.repository.recruitment.AdminRecruitmentSkillRepository;
import io.devridge.core.domain.recruitment.RecruitmentSkill;
import io.devridge.core.domain.roadmap.CourseDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class AdminRecruitmentSkillService {

    private final AdminRecruitmentSkillRepository recruitmentSkillRepository;

    @Transactional(readOnly = true)
    public Page<NotMatchedRequiredSkillsDto> getNotMatchedRecruitmentSkills(Pageable pageable) {
        Page<RecruitmentSkill> recruitmentSkillPage = recruitmentSkillRepository.findByCourseDetailIdIsNull(pageable);

        return recruitmentSkillPage.map(e -> new NotMatchedRequiredSkillsDto(e.getId(), e.getName()));
    }

    @Transactional
    public RecruitmentSkill findByNameOrSave(String recruitmentSkillName, CourseDetail courseDetail) {
        return recruitmentSkillRepository.findByNameIgnoreCaseAndSpacesRemoved(recruitmentSkillName)
                .orElseGet(() -> recruitmentSkillRepository.save(RecruitmentSkill.builder().name(recruitmentSkillName).courseDetail(courseDetail).build()));
    }
}
