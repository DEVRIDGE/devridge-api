package io.devridge.api.web.admin;

import io.devridge.api.dto.admin.company_info.CompanyRequiredAbilityDto;
import io.devridge.api.service.admin.CompanyRequiredAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@RequiredArgsConstructor
@RequestMapping("/admin/requiredability")
@Controller
public class AdminCompanyRequiredController {

    private final CompanyRequiredAdminService companyRequiredAdminService;

    @GetMapping
    public String requiredAbility(Model model, @PageableDefault(size = 10) Pageable pageable) {
        Page<CompanyRequiredAbilityDto> companyRequiredAbilityPageIsNull = companyRequiredAdminService.getCompanyInfoCompanyRequiredListWithCourseDetailIsNull(pageable);
        model.addAttribute("requiredSkillPage", companyRequiredAbilityPageIsNull);
        return "required_ability/required_ability_list";
    }
}
