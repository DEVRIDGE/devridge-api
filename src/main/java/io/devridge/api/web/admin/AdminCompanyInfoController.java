package io.devridge.api.web.admin;

import io.devridge.api.dto.admin.company_info.NeededMakeCompanyInfoDto;
import io.devridge.api.dto.common.ApiResponse;
import io.devridge.api.dto.companyinfo.CompanyInfoForm;
import io.devridge.api.service.admin.AdminCompanyInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@Controller
@RequestMapping("/admin")
public class AdminCompanyInfoController {

    private final AdminCompanyInfoService adminCompanyInfoService;

    @GetMapping
    public String companyInfoRegisterPage(Model model) {
        NeededMakeCompanyInfoDto neededToMakeCompanyInfo = adminCompanyInfoService.getNeededToMakeCompanyInfo();
        model.addAttribute("neededToMakeCompanyInfo", neededToMakeCompanyInfo);

        return "company_info/register_company_info_and_skill";
    }

    @PostMapping("/companyinfo")
    @ResponseBody
    public ResponseEntity<ApiResponse<Object>> saveCompanyInfo(@Valid @RequestBody CompanyInfoForm companyInfoForm) {
        adminCompanyInfoService.registerCompanyInfoAndMakeRoadmap(companyInfoForm);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("회사 정보가 성공적으로 저장되었습니다."));
    }
}
