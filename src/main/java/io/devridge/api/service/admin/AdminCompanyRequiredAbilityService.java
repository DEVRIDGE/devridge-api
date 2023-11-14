package io.devridge.api.service.admin;

import io.devridge.api.domain.companyinfo.*;
import io.devridge.api.domain.roadmap.CourseDetail;
import io.devridge.api.domain.roadmap.CourseDetailRepository;
import io.devridge.api.dto.admin.company_info.CompanyRequiredAbilityDto;
import io.devridge.api.dto.companyinfo.CompanyRequiredAbilityForm;
import io.devridge.api.handler.ex.CompanyNotFoundException;
import io.devridge.api.handler.ex.DetailedPositionNotFoundException;
import io.devridge.api.handler.ex.JobNotFoundException;
import io.devridge.api.service.*;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@Service
public class AdminCompanyRequiredAbilityService {

    private final CompanyRequiredAbilityRepository companyRequiredAbilityRepository;
//    private final CourseDetailRepository courseDetailRepository;
//    private final CompanyInfoService companyInfoService;
//    private final CompanyService companyService;
//    private final JobService jobService;
//    private final DetailedPositionService detailedPositionService;
//    private final CompanyInfoCompanyRequiredAbilityService companyInfoCompanyRequiredAbilityService;
    /*

    public void saveCompanyRequiredAbilities(CompanyRequiredAbilityForm companyRequiredAbilityForm) {
        List<String> companyRequiredAbilityList = companyRequiredAbilityForm.getCompanyRequiredAbilityList();

        Company company = companyService.findByName(companyRequiredAbilityForm.getCompanyName()).orElseThrow(() -> new CompanyNotFoundException());
        Job job = jobService.findByName(companyRequiredAbilityForm.getJobName()).orElseThrow(() -> new JobNotFoundException());
        DetailedPosition detailedPosition = detailedPositionService.findByNameAndCompanyId(companyRequiredAbilityForm.getDetailedPositionName(), company.getId()).orElseThrow(() -> new DetailedPositionNotFoundException());

        CompanyInfo foundCompanyInfo = companyInfoService.validateCompanyInfo(company.getId(), job.getId(), detailedPosition.getId()); // 회사 정보가 존재하는지 검증


        saveCompanyRequiredAbilities(companyRequiredAbilityList, foundCompanyInfo);

    }

    private void saveCompanyRequiredAbilities(List<String> companyRequiredAbilityList, CompanyInfo foundCompanyInfo) {
        for (String companyRequiredAbilityName : companyRequiredAbilityList) {
            CompanyRequiredAbility companyRequiredAbility = null;
            companyRequiredAbility = getCompanyRequiredAbility(companyRequiredAbilityName);

            saveCompanyInfoCompanyRequiredAbility(foundCompanyInfo, companyRequiredAbility);
        }
    }

    private CompanyRequiredAbility getCompanyRequiredAbility(String companyRequiredAbilityName) {
        CompanyRequiredAbility targetCompanyRequiredAbility;
        Optional<CompanyRequiredAbility> companyRequiredAbility = companyRequiredAbilityRepository.findByName(companyRequiredAbilityName);

        if(companyRequiredAbility.isEmpty()) { // 입력받은 회사 요구 역량이 기존에 존재하지 않을 경우 새로 저장한다.
            CompanyRequiredAbility newCompanyRequiredAbility = CompanyRequiredAbility.builder()
                    .name(companyRequiredAbilityName)
                    .courseDetail(null)
                    .build();
            targetCompanyRequiredAbility = companyRequiredAbilityRepository.save(newCompanyRequiredAbility);
        }
        else {
            targetCompanyRequiredAbility = companyRequiredAbility.get();
        }
        return targetCompanyRequiredAbility;
    }

    private void saveCompanyInfoCompanyRequiredAbility(CompanyInfo foundCompanyInfo, CompanyRequiredAbility companyRequiredAbility) {
        Optional<CompanyInfoCompanyRequiredAbility> companyInfoCompanyRequiredAbility = companyInfoCompanyRequiredAbilityService.findByCompanyInfoIdAndCompanyRequiredAbilityId(foundCompanyInfo.getId(), companyRequiredAbility.getId());
        if(companyInfoCompanyRequiredAbility.isEmpty()) { // CompanyInfoCompanyRequiredAbility에 기존에 존재하지 않는 데이터만 저장한다
            CompanyInfoCompanyRequiredAbility newCompanyInfoCompanyRequiredAbility = CompanyInfoCompanyRequiredAbility.builder()
                    .companyInfo(foundCompanyInfo)
                    .companyRequiredAbility(companyRequiredAbility)
                    .build();

            companyInfoCompanyRequiredAbilityService.save(newCompanyInfoCompanyRequiredAbility);
        }
    }

*/
    @Transactional(readOnly = true)
    public Page<CompanyRequiredAbilityDto> getCompanyInfoCompanyRequiredListWithCourseDetailIsNull(Pageable pageable) {
        Page<CompanyRequiredAbility> companyRequiredAbilityPage = companyRequiredAbilityRepository.findByCourseDetailIdIsNull(pageable);

        return companyRequiredAbilityPage.map(entity -> new CompanyRequiredAbilityDto(entity.getId(), entity.getName()));
    }
/*
    @Transactional
    public void matchRequiredAbilityWithCourseDetailId() {
        List<CompanyRequiredAbility> companyRequiredAbilityList = companyRequiredAbilityRepository.findByCourseDetailIdIsNull();
        for (CompanyRequiredAbility companyRequiredAbility : companyRequiredAbilityList) {
            Optional<CourseDetail> courseDetailOptional = courseDetailRepository.findByName(companyRequiredAbility.getName());
            if (courseDetailOptional.isEmpty()) {
                continue;
            }
            companyRequiredAbility.changeCourseDetail(courseDetailOptional.get());
        }
    }

    private String processStringCutting(String name) {
        // 모든 공백 제거
        String noSpaces = name.replaceAll("\\s+", "");

        // 영어 문자는 소문자로 변환하고, 다른 문자는 그대로 유지
        StringBuilder result = new StringBuilder();
        for (char c : noSpaces.toCharArray()) {
            if (Character.isAlphabetic(c) && Character.isUpperCase(c)) {
                result.append(Character.toLowerCase(c));
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }

     */
}
