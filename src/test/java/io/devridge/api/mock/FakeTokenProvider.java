package io.devridge.api.mock;

import io.devridge.api.domain.user.User;
import io.devridge.api.util.jwt.TokenProvider;
import io.devridge.api.util.jwt.exception.JwtExpiredException;
import io.devridge.api.util.jwt.exception.JwtIdConversionException;
import io.devridge.api.util.jwt.exception.JwtNotHaveIdException;
import io.devridge.api.util.jwt.exception.JwtVerifyException;

import java.time.LocalDateTime;
import java.util.Date;

public class FakeTokenProvider implements TokenProvider {
    public String token;
    public String userId;
    public LocalDateTime currentAt;
    public LocalDateTime expiredAt;

    @Override
    public String createAccessToken(User user, Date expiredAt) {
        return "access_" + token;
    }

    @Override
    public String createRefreshToken(Date now, Date expiredAt) {
        return "refresh_" + token;
    }

    @Override
    public Long verifyAndGetUserId(String token) {
        if (!token.equals(this.token)) {
            throw new JwtVerifyException("검증에 실패하였습니다.");
        }
        if (expiredAt != null && currentAt != null && currentAt.isAfter(expiredAt)) {
            throw new JwtExpiredException("토큰이 만료되었습니다.");
        }
        if (userId == null) {
            throw new JwtNotHaveIdException();
        }
        try {
            return Long.parseLong(userId);
        } catch (NumberFormatException e) {
            throw new JwtIdConversionException("long 값으로 변환할 수 없습니다.");
        }
    }

    @Override
    public void validateToken(String token) {
        if (!token.equals(this.token)) {
            throw new JwtVerifyException("검증에 실패하였습니다.");
        }
        if (expiredAt != null && currentAt != null && currentAt.isAfter(expiredAt)) {
            throw new JwtExpiredException("토큰이 만료되었습니다.");
        }
    }
}
