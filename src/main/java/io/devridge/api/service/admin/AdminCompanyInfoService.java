package io.devridge.api.service.admin;

import io.devridge.api.domain.companyinfo.*;
import io.devridge.api.domain.roadmap.*;
import io.devridge.api.dto.admin.company_info.NeededMakeCompanyInfoDto;
import io.devridge.api.dto.companyinfo.CompanyInfoForm;
import io.devridge.api.handler.ex.JobNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Service
public class AdminCompanyInfoService {
    private final CompanyRepository companyRepository;
    private final JobRepository jobRepository;
    private final DetailedPositionRepository detailedPositionRepository;
    private final CompanyJobRepository companyJobRepository;
    private final JobDetailedPositionRepository jobDetailedPositionRepository;
    private final CompanyInfoRepository companyInfoRepository;
    private final CourseDetailRepository courseDetailRepository;
    private final CompanyRequiredAbilityRepository companyRequiredAbilityRepository;
    private final CompanyInfoCompanyRequiredAbilityRepository companyInfoCompanyRequiredAbilityRepository;
    private final CourseRepository courseRepository;
    private final RoadmapRepository roadmapRepository;

    public NeededMakeCompanyInfoDto getNeededToMakeCompanyInfo() {
        List<Company> allCompanyList = getAllCompanyAndDetailPosition();
        List<Job> allJobList = getAllJob();

        return new NeededMakeCompanyInfoDto(allCompanyList, allJobList);
    }

    /**
     * 회사공고, 채용스킬 등록 및 로드맵 생성 자동화
     */
    public void registerCompanyInfoAndMakeRoadmap(CompanyInfoForm companyInfoForm) {
        // 회사, 직무, 상세직무 등록
        Company company = findCompanyOrSave(companyInfoForm.getCompanyName());
        Job job = findJob(companyInfoForm.getJobName());
        DetailedPosition detailedPosition = findDetailPositionOrSave(companyInfoForm.getDetailedPositionName(), company);
        associateCompanyAndJobAndDetailedPosition(company, job, detailedPosition); // 회사, 직무, 상세직무 연관관계 등록

        // 회사공고 등록
        CompanyInfo companyInfo = saveCompanyInfo(company, job, detailedPosition, companyInfoForm.getCompanyInfoUrl());

        // 채용스킬 등록
        List<CompanyRequiredAbility> companyRequiredAbilities = saveCompanyRequiredAbility(companyInfoForm.getCompanyRequiredAbilityList());
        associateCompanyInfoAndCompanyRequiredAbility(companyInfo, companyRequiredAbilities);

        // 로드맵 등록
        makeRoadmap(job, companyInfo, companyRequiredAbilities);
    }

    private List<Job> getAllJob() {
        return jobRepository.findAll();
    }

    private List<Company> getAllCompanyAndDetailPosition() {
        return companyRepository.findAllByFetch();
    }

    private Company findCompanyOrSave(String companyName) {
        return companyRepository.findByNameByFetch(companyName)
                .orElseGet(() -> companyRepository.save(Company.builder().name(companyName).build()));
    }

    private Job findJob(String jobName) {
        return jobRepository.findByName(jobName)
                .orElseThrow(JobNotFoundException::new);
    }

    private DetailedPosition findDetailPositionOrSave(String detailedPositionName, Company company) {
        return company.getDetailedPositionList().stream().filter(detailedPosition -> detailedPosition.getName().equals(detailedPositionName)).findFirst()
                .orElseGet(() -> detailedPositionRepository.save(DetailedPosition.builder().name(detailedPositionName).company(company).build()));
    }

    private void associateCompanyAndJobAndDetailedPosition(Company company, Job job, DetailedPosition detailedPosition) {
        associateCompanyAndJob(company, job);
        associateJobAndDetailedPosition(job, detailedPosition);
    }

    private void associateJobAndDetailedPosition(Job job, DetailedPosition detailedPosition) {
        jobDetailedPositionRepository.findByJobAndDetailedPosition(job, detailedPosition)
                .orElseGet(() -> jobDetailedPositionRepository.save(JobDetailedPosition.builder().job(job).detailedPosition(detailedPosition).build()));
    }

    private void associateCompanyAndJob(Company company, Job job) {
        companyJobRepository.findByCompanyAndJob(company, job)
                .orElseGet(() -> companyJobRepository.save(CompanyJob.builder().company(company).job(job).build()));
    }

    private CompanyInfo saveCompanyInfo(Company company, Job job, DetailedPosition detailedPosition, String companyInfoUrl) {
        /**
         * TODO 회사정보 중복 체크
         * 임시적으로 중복 체크 기능 추가
         * 메인 API 중복 데이터 처리가 끝나면 해당 로직 삭제
         */
        Optional<CompanyInfo> companyInfoOptional = companyInfoRepository.findByCompanyIdAndJobIdAndDetailedPositionId(company.getId(), job.getId(), detailedPosition.getId());
        if(companyInfoOptional.isPresent()) {
            throw new IllegalArgumentException("이미 등록된 회사정보입니다.");
        }
        return companyInfoRepository.save(CompanyInfo.builder()
                .company(company)
                .job(job)
                .detailedPosition(detailedPosition)
                .content(companyInfoUrl)
                .build());
    }

    private List<CompanyRequiredAbility> saveCompanyRequiredAbility(List<String> companyRequiredAbilityList) {
        return companyRequiredAbilityList.stream()
                .map(this::createOrFindCompanyRequiredAbility)
                .collect(Collectors.toList());
    }

    private CompanyRequiredAbility createOrFindCompanyRequiredAbility(String companyRequiredAbilityName) {
        return companyRequiredAbilityRepository
                .findByNameIgnoreCaseAndSpacesRemoved(companyRequiredAbilityName)
                .orElseGet(() -> companyRequiredAbilityRepository.save(CompanyRequiredAbility.builder()
                        .name(companyRequiredAbilityName)
                        .courseDetail(courseDetailRepository.findByNameIgnoringCaseAndSpaces(companyRequiredAbilityName).orElse(null)).build())
                );
    }

    private void associateCompanyInfoAndCompanyRequiredAbility(CompanyInfo companyInfo, List<CompanyRequiredAbility> companyRequiredAbilities) {
        companyInfoCompanyRequiredAbilityRepository.saveAll(
                companyRequiredAbilities.stream()
                        .map(companyRequiredAbility -> CompanyInfoCompanyRequiredAbility.builder()
                                .companyInfo(companyInfo)
                                .companyRequiredAbility(companyRequiredAbility)
                                .build())
                        .collect(Collectors.toList())
        );
    }

    private void makeRoadmap(Job job, CompanyInfo companyInfo, List<CompanyRequiredAbility> companyRequiredAbilities) {
        List<Course> courseList = courseRepository.getCourseByJobOrderByOrder(job);
        List<Roadmap> roadmaps = courseList.stream()
                .map(course -> {
                    boolean isMatch = course.getCourseToDetails().stream()
                            .anyMatch(courseToDetail -> companyRequiredAbilities.stream()
                                    .anyMatch(ability -> ability.getCourseDetail() != null &&
                                            ability.getCourseDetail().getId().equals(courseToDetail.getCourseDetail().getId())));
                    return Roadmap.builder().course(course).companyInfo(companyInfo).matchingFlag(isMatch ? MatchingFlag.YES : MatchingFlag.NO).build();
                })
                .collect(Collectors.toList());
        roadmapRepository.saveAll(roadmaps);
    }
}
