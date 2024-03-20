package io.devridge.api.util.jwt;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class TokenDto {
    private final String tokenContent;
    private final LocalDateTime expiredAt;

    @Builder
    public TokenDto(String tokenContent, LocalDateTime expiredAt) {
        this.tokenContent = tokenContent;
        this.expiredAt = expiredAt;
    }
}
