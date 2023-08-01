package io.devridge.api.web;

import io.devridge.api.dto.CompanyResponseDto;
import io.devridge.api.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    @GetMapping("/companies")
    public CompanyResponseDto companyList() {

        return companyService.companyList();
    }

}
