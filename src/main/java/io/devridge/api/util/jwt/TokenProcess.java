package io.devridge.api.util.jwt;

import io.devridge.api.domain.user.User;
import io.devridge.api.util.jwt.exception.JwtExpiredException;
import io.devridge.api.util.jwt.exception.JwtVerifyException;
import io.devridge.api.util.time.TimeProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;

import static io.devridge.api.util.jwt.JwtSetting.ACCESS_TOKEN_VALID_TIME;
import static io.devridge.api.util.jwt.JwtSetting.REFRESH_TOKEN_VALID_TIME;

@Slf4j
@RequiredArgsConstructor
@Component
public class TokenProcess {

    private final TimeProvider timeProvider;
    private final TokenProvider tokenProvider;

    public TokenDto createAccessToken(User user) {
        LocalDateTime tokenExpiredAt = timeProvider.getCurrentTime().plus(Duration.ofMillis(ACCESS_TOKEN_VALID_TIME));
        Date tokenExpiredDate = timeProvider.convertToJavaUtilDate(tokenExpiredAt);
        String accessToken = tokenProvider.createAccessToken(user, tokenExpiredDate);

        return TokenDto.builder()
                .token(accessToken)
                .expiredAt(tokenExpiredAt)
                .build();
    }

    public TokenDto createRefreshToken() {
        LocalDateTime currentTime = timeProvider.getCurrentTime();
        LocalDateTime tokenExpiredAt = currentTime.plus(Duration.ofMillis(REFRESH_TOKEN_VALID_TIME));
        String refreshToken = tokenProvider.createRefreshToken(timeProvider.convertToJavaUtilDate(currentTime), timeProvider.convertToJavaUtilDate(tokenExpiredAt));

        return TokenDto.builder()
                .token(refreshToken)
                .expiredAt(tokenExpiredAt)
                .build();
    }

    public Long verifyAndGetUserId(String token) {
        return tokenProvider.verifyAndGetUserId(token);
    }

    public boolean isTokenValid(String token) {
        try {
            tokenProvider.validateToken(token);
            return true;
        } catch (JwtVerifyException | JwtExpiredException exception) {
            log.error("isTokenValid exception = {}", exception.getMessage());
            return false;
        }
    }

    public void tokenValidOrThrowException(String token) {
        tokenProvider.validateToken(token);
    }
}
