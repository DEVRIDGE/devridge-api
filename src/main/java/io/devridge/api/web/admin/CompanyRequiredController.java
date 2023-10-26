package io.devridge.api.web.admin;

import io.devridge.api.dto.common.ApiResponse;
import io.devridge.api.service.admin.CompanyRequiredService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/admin/api")
@RestController
public class CompanyRequiredController {
    private final CompanyRequiredService companyRequiredService;

    @PatchMapping("/requiredAbility")
    public ResponseEntity<ApiResponse<Object>> matchRequiredAbility() {

        companyRequiredService.matchRequiredAbilityWithCourseDetailId();
        return ResponseEntity.status(200).body(ApiResponse.success("등록되었습니다."));
    }
}
