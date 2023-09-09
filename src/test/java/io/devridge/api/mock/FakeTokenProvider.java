package io.devridge.api.mock;

import io.devridge.api.domain.user.User;
import io.devridge.api.util.jwt.TokenProvider;

import java.time.LocalDateTime;
import java.util.Date;

public class FakeTokenProvider implements TokenProvider {
    public String token;
    public Long userId;
    public LocalDateTime currentAt;
    public LocalDateTime expiredAt;
    public String error;

    public void init() {
        this.token = null;
        this.userId = null;
        this.currentAt = null;
        this.expiredAt = null;
        this.error = null;
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
