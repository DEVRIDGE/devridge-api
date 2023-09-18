package io.devridge.api.web;

import io.devridge.api.dto.common.ApiResponse;
import io.devridge.api.dto.token.ReissueTokenRequestDto;
import io.devridge.api.dto.token.ReissueTokenResponseDto;
import io.devridge.api.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class TokenController {

    private final TokenService tokenService;

    @PostMapping("/token/reissue")
    public ResponseEntity<ApiResponse<ReissueTokenResponseDto>> reissueToken(@RequestBody ReissueTokenRequestDto reissueTokenRequestDto) {
        ReissueTokenResponseDto accessTokenDto = tokenService.reissue(reissueTokenRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("발급 완료", accessTokenDto));
    }
}
