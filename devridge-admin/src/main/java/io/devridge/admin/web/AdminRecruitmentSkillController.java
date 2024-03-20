package io.devridge.admin.web;

import io.devridge.admin.dto.recruitment.NotMatchedRequiredSkillsDto;
import io.devridge.admin.service.recruitment.AdminRecruitmentSkillService;
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
public class AdminRecruitmentSkillController {

    private final AdminRecruitmentSkillService adminRecruitmentSkillService;

    @GetMapping
    public String notMatchedRequiredAbilityView(Model model, @PageableDefault(size = 10) Pageable pageable) {
        Page<NotMatchedRequiredSkillsDto> notMatchedRequiredSkills = adminRecruitmentSkillService.getNotMatchedRecruitmentSkills(pageable);
        model.addAttribute("requiredSkillPage", notMatchedRequiredSkills);

        return "required_ability/required_ability_list";
    }
}
