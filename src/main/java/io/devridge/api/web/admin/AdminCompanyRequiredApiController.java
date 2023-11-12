package io.devridge.api.web.admin;

import io.devridge.api.dto.common.ApiResponse;
import io.devridge.api.service.admin.AdminCompanyRequiredAbilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/admin/api")
@RestController
public class AdminCompanyRequiredApiController {
    private final AdminCompanyRequiredAbilityService adminCompanyRequiredAbilityService;

    @PatchMapping("/requiredAbility")
    public ResponseEntity<ApiResponse<Object>> matchRequiredAbility() {

        //adminCompanyRequiredAbilityService.matchRequiredAbilityWithCourseDetailId();
        return ResponseEntity.status(200).body(ApiResponse.success("등록되었습니다."));
    }
}
