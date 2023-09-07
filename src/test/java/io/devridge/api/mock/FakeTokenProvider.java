package io.devridge.api.mock;

import io.devridge.api.domain.user.User;
import io.devridge.api.util.jwt.TokenProvider;

import java.util.Date;

public class FakeTokenProvider implements TokenProvider {
    public String token;

    public FakeTokenProvider(String token) {
        this.token = token;
    }

    @Override
    public String createAccessToken(User user, Date expiredAt) {
        return "access_" + token;
    }

    @Override
    public String createRefreshToken(Date now, Date expiredAt) {
        return "refresh_" + token;
    }

    @Override
    public Long verify(String token) {
        return null;
    }
}
