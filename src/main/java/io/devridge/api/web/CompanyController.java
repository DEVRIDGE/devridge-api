package io.devridge.api.web;

import io.devridge.api.dto.CompanyResponseDto;
import io.devridge.api.dto.common.ApiResponse;
import io.devridge.api.service.CompanyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    @GetMapping("/companies")
    public ResponseEntity<ApiResponse<Object>> companyList() {
        CompanyResponseDto companyResponseDto = companyService.companyList();
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(companyResponseDto));
    }
}
