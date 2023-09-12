package io.devridge.api.web;

import io.devridge.api.dto.common.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
public class testController {

    @GetMapping("/token/check")
    public ResponseEntity<ApiResponse<Object>> aa(HttpServletRequest request) {
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
                    return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("쿠키값 있음", cookieValue));
                }
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error("헤더에서 토큰이 없습니다"));
    }
}
