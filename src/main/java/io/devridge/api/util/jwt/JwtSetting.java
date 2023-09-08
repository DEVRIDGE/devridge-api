package io.devridge.api.util.jwt;

public class JwtSetting {
    public static final long ACCESS_TOKEN_VALID_TIME = 1000L * 60 * 30; // 30분
    public static  final long REFRESH_TOKEN_VALID_TIME = 1000L * 60 * 60 * 24 * 7; // 7일
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String JWT_HEADER = "Authorization";
}
