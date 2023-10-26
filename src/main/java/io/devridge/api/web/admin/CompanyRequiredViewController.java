package io.devridge.api.web.admin;

import io.devridge.api.dto.admin.CompanyRequiredAbilityListByCourseDetailNullDto;
import io.devridge.api.service.admin.CompanyRequiredService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@RequiredArgsConstructor
@RequestMapping("/admin")
@Controller
public class CompanyRequiredViewController {

    private final CompanyRequiredService companyRequiredService;

    @GetMapping("/requiredability")
    public String requiredAbility(Model model) {
        CompanyRequiredAbilityListByCourseDetailNullDto companyInfoCompanyRequiredListWithCourseDetailIsNull = companyRequiredService.getCompanyInfoCompanyRequiredListWithCourseDetailIsNull();
        model.addAttribute("requiredSkillList", companyInfoCompanyRequiredListWithCourseDetailIsNull.getCompanyRequiredAbilityDtoList());
        return "requiredAbilityList";
    }
}
