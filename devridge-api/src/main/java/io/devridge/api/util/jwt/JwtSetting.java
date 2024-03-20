package io.devridge.api.util.jwt;

public class JwtSetting {
    // 실제 사용되는 설정값들
    public static final long ACCESS_TOKEN_VALID_TIME = 1000L * 60 * 30; // 30분
    public static final long REFRESH_TOKEN_VALID_TIME = 1000L * 60 * 60 * 24 * 7; // 7일
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String JWT_HEADER = "Authorization";

    // 프론트나 외부에 전달될 표시용 설정값들
    public static final String DISPLAY_TOKEN_PREFIX = "Bearer";
    public static final long DISPLAY_ACCESS_TOKEN_DURATION =  60 * 30;
}
