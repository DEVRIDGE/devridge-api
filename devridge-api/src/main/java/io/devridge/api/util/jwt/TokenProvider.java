package io.devridge.api.util.jwt;

import io.devridge.core.domain.user.User;

import java.util.Date;

public interface TokenProvider {

    String createAccessToken(User user, Date expiredAt);

    String createRefreshToken(Date now, Date expiredAt);

    Long verifyAndGetUserId(String token);

    void validateToken(String token);
}
