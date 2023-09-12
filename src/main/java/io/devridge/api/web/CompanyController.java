package io.devridge.api.web;

import io.devridge.api.dto.CompanyResponseDto;
import io.devridge.api.dto.common.ApiResponse;
import io.devridge.api.service.CompanyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;


@Slf4j
@RestController
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    @GetMapping("/companies")
    public ResponseEntity<ApiResponse<Object>> companyList(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        log.info("들어옴");
        if (cookies != null) {
            log.info("들어옴2");
            for (Cookie cookie : cookies) {
                if ("refreshToken".equals(cookie.getName())) {
                    log.info("cookie.getName() = {}", cookie.getName());
                    String cookieValue = cookie.getValue();
                    log.info("cookieValue = {}", cookieValue);
                    // 쿠키 값을 처리하는 로직
                }
            }
        }

        CompanyResponseDto companyResponseDto = companyService.companyList();
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(companyResponseDto));
    }
}
