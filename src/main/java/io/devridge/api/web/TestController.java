package io.devridge.api.web;

import io.devridge.api.dto.common.ApiResponse;
import io.devridge.api.dto.token.ReissueTokenResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
public class TestController {

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

    @GetMapping("/token/apply/1")
    public ResponseEntity<ApiResponse<Object>> applyToken1(HttpServletResponse response) {
        // 쿠키 생성 및 설정
        int expiresInDays = 7;
        int expiresInSeconds = expiresInDays * 24 * 60 * 60;

        ResponseCookie cookie = ResponseCookie.from("test", "test123")
                .domain("localhost")
                .sameSite("None")
                .secure(true)
                .path("/")
                .httpOnly(false)
                .maxAge(expiresInSeconds)
                .build();
        // 응답에 쿠키 추가
        response.addHeader("Set-Cookie", cookie.toString());

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("발급 완료"));
    }
}
