package io.devridge.api.web.admin;

import io.devridge.api.dto.common.ApiResponse;
import io.devridge.api.dto.companyinfo.CompanyInfoForm;
import io.devridge.api.dto.companyinfo.CompanyRequiredAbilityForm;
import io.devridge.api.service.admin.AdminCompanyInfoService;
import io.devridge.api.service.admin.AdminCompanyRequiredAbilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Controller
@RequestMapping("/admin")
public class AdminCompanyInfoController {

    private final AdminCompanyInfoService adminCompanyInfoService;
    private final AdminCompanyRequiredAbilityService adminCompanyRequiredAbilityService;

    @GetMapping
    public String companyInfoRegisterPage() {


        return "company_info/register_company_info_and_skill";
    }

    @PostMapping("/companyinfo")
    @ResponseBody
    public ResponseEntity<ApiResponse<Object>> saveCompanyInfo(@Validated @RequestBody CompanyInfoForm companyInfoForm) {

        //adminCompanyInfoService.transferCompanyInfoToAssociatedTable(companyInfoForm);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("회사 정보가 성공적으로 저장되었습니다."));
    }

    @PostMapping("/companyinfo/requiredability")
    @ResponseBody
    public ResponseEntity<ApiResponse<Object>> saveCompanyRequiredAbility(@Validated @RequestBody CompanyRequiredAbilityForm companyRequiredAbilityForm) {

        //adminCompanyRequiredAbilityService.saveCompanyRequiredAbilities(companyRequiredAbilityForm);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(companyRequiredAbilityForm.getCompanyName() + "회사의 " +
                        companyRequiredAbilityForm.getJobName() + "직무의 " +
                        companyRequiredAbilityForm.getDetailedPositionName() + "서비스에 해당하는" +
                "회사 정보에 대한 회사 요구 기술이 성공적으로 저장되었습니다."));
    }
}
