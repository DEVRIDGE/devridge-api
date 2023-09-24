package io.devridge.api.web.admin;

import io.devridge.api.dto.common.ApiResponse;
import io.devridge.api.dto.companyinfo.CompanyInfoDto;
import io.devridge.api.service.CompanyInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CompanyInfoController {

    private final CompanyInfoService companyInfoService;
    
    @PostMapping("/companyinfo/save")
    public ResponseEntity<ApiResponse<Object>> saveCompanyInfo(@RequestBody CompanyInfoDto companyInfoDto) {

        companyInfoService.transferCompanyInfoToAssociatedTable(companyInfoDto);



        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("회사 정보가 성공적으로 저장되었습니다."));
    }
}
