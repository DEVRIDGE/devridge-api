package io.devridge.api.web;

import io.devridge.api.dto.CompanyResponseDto;
import io.devridge.api.dto.common.ApiResponse;
import io.devridge.api.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;


@RestController
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    @GetMapping("/companies")
    public ResponseEntity<ApiResponse<Object>> companyList(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refreshToken".equals(cookie.getName())) {
                    System.out.println("cookie.getName() = " + cookie.getName());
                    String cookieValue = cookie.getValue();
                    System.out.println("cookieValue = " + cookieValue);
                    // 쿠키 값을 처리하는 로직
                }
            }
        }

        CompanyResponseDto companyResponseDto = companyService.companyList();
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(companyResponseDto));
    }
}
