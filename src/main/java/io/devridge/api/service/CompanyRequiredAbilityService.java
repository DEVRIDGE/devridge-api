package io.devridge.api.service;

import io.devridge.api.domain.companyinfo.*;
import io.devridge.api.dto.companyinfo.CompanyRequiredAbilityForm;
import io.devridge.api.handler.ex.CompanyNotFoundException;
import io.devridge.api.handler.ex.DetailedPositionNotFoundException;
import io.devridge.api.handler.ex.JobNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CompanyRequiredAbilityService {

    private final CompanyInfoService companyInfoService;
    private final CompanyService companyService;
    private final JobService jobService;
    private final DetailedPositionService detailedPositionService;
    private final CompanyInfoCompanyRequiredAbilityService companyInfoCompanyRequiredAbilityService;

    private final CompanyRequiredAbilityRepository companyRequiredAbilityRepository;

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
}
