package io.devridge.api.util.jwt;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class TokenDto {
    private final String token;
    private final LocalDateTime expiredAt;

    @Builder
    public TokenDto(String token, LocalDateTime expiredAt) {
        this.token = token;
        this.expiredAt = expiredAt;
    }
}
