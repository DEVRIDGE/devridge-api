package io.devridge.admin.web;

import io.devridge.admin.dto.common.ApiResponse;
import io.devridge.admin.dto.company.AllCompanyAndJobAndDetailPositionDto;

import io.devridge.admin.dto.recruitment.RecruitmentInfoForm;
import io.devridge.admin.service.recruitment.AdminRecruitmentInfoService;
import io.devridge.admin.service.company.AdminCompanyService;
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
public class AdminRecruitmentInfoController {

    private final AdminCompanyService adminCompanyService;
    private final AdminRecruitmentInfoService adminRecruitmentInfoService;

    @GetMapping
    public String getAddRecruitmentInfoView(Model model) {
        AllCompanyAndJobAndDetailPositionDto allCompanyAndJobAndDetailPositionDto = adminCompanyService.getCompanyAndJobAndDetailPositionList();
        model.addAttribute("neededToMakeCompanyInfo", allCompanyAndJobAndDetailPositionDto);

        return "recruitment_Info/main";
    }

    @PostMapping("/recruitmentinfo")
    @ResponseBody
    public ResponseEntity<ApiResponse<Object>> saveCompanyInfo(@Valid @RequestBody RecruitmentInfoForm companyInfoForm) {
        adminRecruitmentInfoService.registerCompanyInfoAndMakeRoadmap(companyInfoForm);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("회사 정보가 성공적으로 저장되었습니다."));
    }
}
