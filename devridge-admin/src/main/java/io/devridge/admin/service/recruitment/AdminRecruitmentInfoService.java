package io.devridge.admin.service.recruitment;

import io.devridge.admin.dto.recruitment.RecruitmentInfoForm;
import io.devridge.admin.handler.ex.AdminRecruitmentInfoException;
import io.devridge.admin.repository.recruitment.AdminRecruitmentInfoAndSkillRepository;
import io.devridge.admin.repository.recruitment.AdminRecruitmentInfoRepository;
import io.devridge.admin.service.company.AdminCompanyService;
import io.devridge.admin.service.company.AdminDetailedPositionService;
import io.devridge.admin.service.company.AdminJobService;
import io.devridge.admin.service.course.AdminCourseDetailService;
import io.devridge.admin.service.course.AdminRoadmapService;
import io.devridge.core.domain.company.*;
import io.devridge.core.domain.recruitment.RecruitmentInfo;
import io.devridge.core.domain.recruitment.RecruitmentInfoAndSkill;
import io.devridge.core.domain.recruitment.RecruitmentSkill;
import io.devridge.core.domain.roadmap.CourseDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor

@Service
public class AdminRecruitmentInfoService {

    private final AdminCompanyService companyService;
    private final AdminJobService jobService;
    private final AdminDetailedPositionService detailedPositionService;
    private final AdminCourseDetailService courseDetailService;
    private final AdminRecruitmentSkillService recruitmentSkillService;
    private final AdminRoadmapService roadmapService;

    private final AdminRecruitmentInfoRepository recruitmentInfoRepository;
    private final AdminRecruitmentInfoAndSkillRepository recruitmentInfoAndSkillRepository;

    /**
     * 회사공고, 채용스킬 등록 및 로드맵 생성 자동화
     */
    @Transactional
    public void registerCompanyInfoAndMakeRoadmap(RecruitmentInfoForm recruitmentInfoForm) {
        // 회사, 직무, 상세직무 등록
        Company company = findCompanyOrSave(recruitmentInfoForm.getCompanyName());
        Job job = findJob(recruitmentInfoForm.getJobName());
        DetailedPosition detailedPosition = findDetailPositionOrSave(recruitmentInfoForm.getDetailedPositionName(), company);
        associateCompanyAndJobAndDetailedPosition(company, job, detailedPosition); // 회사, 직무, 상세직무 연관관계 등록

        // 채용 공고, 스킬 등록
        RecruitmentInfo recruitmentInfo = saveRecruitmentInfo(company, job, detailedPosition, recruitmentInfoForm.getCompanyInfoUrl());
        List<RecruitmentSkill> recruitmentSkills = saveRecruitmentSkill(recruitmentInfoForm.getCompanyRequiredAbilityList());
        associateRecruitmentInfoAndRecruitmentSkills(recruitmentInfo, recruitmentSkills); // 채용 공고, 스킬 연관 관계 등록

        // 로드맵 등록
        makeRoadmap(job, recruitmentInfo, recruitmentSkills);
    }

    private Company findCompanyOrSave(String companyName) {
        return companyService.findCompanyOrSave(companyName);
    }

    private Job findJob(String jobName) {
        return jobService.findByName(jobName);
    }

    private DetailedPosition findDetailPositionOrSave(String detailedPositionName, Company company) {
        return company.getDetailedPositionList().stream().filter(detailedPosition -> detailedPosition.getName().equals(detailedPositionName)).findFirst()
                .orElseGet(() -> detailedPositionService.saveDetailPosition(detailedPositionName, company));
    }

    private void associateCompanyAndJobAndDetailedPosition(Company company, Job job, DetailedPosition detailedPosition) {
        companyService.associateCompanyAndJob(company, job);
        jobService.associateJobAndDetailedPosition(job, detailedPosition);
    }

    private RecruitmentInfo saveRecruitmentInfo(Company company, Job job, DetailedPosition detailedPosition, String recruitmentInfoUrl) {
        Optional<RecruitmentInfo> optionalRecruitmentInfo = recruitmentInfoRepository.findByCompanyAndJobAndDetailedPositionWithFetch(company, job, detailedPosition);
        if (optionalRecruitmentInfo.isPresent()) {
            RecruitmentInfo recruitmentInfo = optionalRecruitmentInfo.get();
            throw new AdminRecruitmentInfoException.AdminDuplicateRecruitmentInfo(recruitmentInfo.getId());
        }

        return recruitmentInfoRepository.save(RecruitmentInfo.builder()
                .company(company)
                .job(job)
                .detailedPosition(detailedPosition)
                .content(recruitmentInfoUrl)
                .build());
    }

    private List<RecruitmentSkill> saveRecruitmentSkill(List<String> recruitmentSkillList) {
        return recruitmentSkillList.stream()
                .map(this::findCompanyRequiredAbilityOrSave)
                .collect(Collectors.toList());
    }

    private RecruitmentSkill findCompanyRequiredAbilityOrSave(String skillName) {
        CourseDetail courseDetail = courseDetailService.findByNameIgnoringSpace(skillName);
        return recruitmentSkillService.findByNameOrSave(skillName, courseDetail);
    }

    private void associateRecruitmentInfoAndRecruitmentSkills(RecruitmentInfo recruitmentInfo, List<RecruitmentSkill> recruitmentSkillList) {
        List<RecruitmentInfoAndSkill> recruitmentInfoAndSkillList = recruitmentSkillList.stream()
                .map(recruitmentSkill -> RecruitmentInfoAndSkill.builder().recruitmentInfo(recruitmentInfo).recruitmentSkill(recruitmentSkill)
                        .build())
                .collect(Collectors.toList());

        recruitmentInfoAndSkillRepository.saveAll(recruitmentInfoAndSkillList);
    }

    private void makeRoadmap(Job job, RecruitmentInfo recruitmentInfo, List<RecruitmentSkill> recruitmentSkillList) {
        roadmapService.makeRoadmap(job, recruitmentInfo, recruitmentSkillList);
    }
}
