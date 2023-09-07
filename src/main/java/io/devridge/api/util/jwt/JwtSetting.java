package io.devridge.api.util.jwt;

import lombok.Getter;
import org.springframework.stereotype.Component;

@Getter
@Component
public class JwtSetting {
    private final long accessTokenValidTime = 1000L * 60 * 30; // 30분
    private final long refreshTokenValidTime = 1000L * 60 * 60 * 24 * 7; // 7일
}
