package io.devridge.api.util.jwt;

import io.devridge.api.domain.user.User;

import java.util.Date;

public interface TokenProvider {

    String createAccessToken(User user, Date expiredAt);

    String createRefreshToken(Date now, Date expiredAt);

    Long verifyAndGetUserId(String token);

    boolean isTokenValid(String token);
}
