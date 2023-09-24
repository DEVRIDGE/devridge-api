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

@Service
@Transactional
@RequiredArgsConstructor
public class CompanyRequiredAbilityService {

    private final CompanyInfoService companyInfoService;
    private final CompanyService companyService;
    private final JobService jobService;
    private final DetailedPositionService detailedPositionService;

    private final CompanyRequiredAbilityRepository companyRequiredAbilityRepository;

    public void saveCompanyRequiredAbilities(CompanyRequiredAbilityForm companyRequiredAbilityForm) {
        List<String> companyRequiredAbilityList = companyRequiredAbilityForm.getCompanyRequiredAbilityList();

        Company company = companyService.findByName(companyRequiredAbilityForm.getCompanyName()).orElseThrow(() -> new CompanyNotFoundException());
        Job job = jobService.findByName(companyRequiredAbilityForm.getJobName()).orElseThrow(() -> new JobNotFoundException());
        DetailedPosition detailedPosition = detailedPositionService.findByNameAndCompanyId(companyRequiredAbilityForm.getDetailedPositionName(), company.getId()).orElseThrow(() -> new DetailedPositionNotFoundException());

        CompanyInfo foundCompanyInfo = companyInfoService.validateCompanyInfo(company.getId(), job.getId(), detailedPosition.getId()); // 회사 정보가 존재하는지 검증


        // 해당 회사 정보에 대해서 입력으로 들어온 요구 역량들을 저장한다. 다만, 요구 기술과 대응되는 courseDetail에는 null이 들어간 상태로 저장된다.
        saveCompanyRequiredAbilities(companyRequiredAbilityList, foundCompanyInfo);
    }

    private void saveCompanyRequiredAbilities(List<String> companyRequiredAbilityList, CompanyInfo foundCompanyInfo) {
        for(String requiredAbilityName : companyRequiredAbilityList) {
            if(companyRequiredAbilityRepository.findByNameAndCompanyInfoId(requiredAbilityName, foundCompanyInfo.getId()).isEmpty()) {
                CompanyRequiredAbility newCompanyRequiredAbility = CompanyRequiredAbility.builder()
                        .companyInfo(foundCompanyInfo)
                        .name(requiredAbilityName)
                        .build();
                companyRequiredAbilityRepository.save(newCompanyRequiredAbility);
            }
        }
    }
}
