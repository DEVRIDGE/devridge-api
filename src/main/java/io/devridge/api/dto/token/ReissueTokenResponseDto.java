package io.devridge.api.dto.token;

import io.devridge.api.util.jwt.TokenDto;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

import static io.devridge.api.util.jwt.JwtSetting.DISPLAY_ACCESS_TOKEN_DURATION;
import static io.devridge.api.util.jwt.JwtSetting.DISPLAY_TOKEN_PREFIX;

@Getter
public class ReissueTokenResponseDto {
    private final String accessToken;
    private final String tokenType = DISPLAY_TOKEN_PREFIX;
    private final Long expiresIn = DISPLAY_ACCESS_TOKEN_DURATION;
    private final LocalDateTime expiredAt;

    @Builder
    public ReissueTokenResponseDto(TokenDto tokenDto) {
        this.accessToken = tokenDto.getToken();
        this.expiredAt = tokenDto.getExpiredAt();
    }
}
