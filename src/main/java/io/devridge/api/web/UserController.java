package io.devridge.api.web;

import io.devridge.api.config.auth.LoginUser;
import io.devridge.api.dto.UserInfoForChannelTalkResponseDto;
import io.devridge.api.dto.common.ApiResponse;
import io.devridge.api.dto.token.ReissueTokenResponseDto;
import io.devridge.api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/users/channeltalkinfo")
    public ResponseEntity<ApiResponse<Object>> getUserInfoForChannelTalk(@AuthenticationPrincipal LoginUser loginUser) {

        UserInfoForChannelTalkResponseDto userInfoForChannelTalk = userService.getUserInfoForChannelTalk(loginUser);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("로그인한 사용자의 채널톡을 위한 정보입니다.", userInfoForChannelTalk));
    }
}
