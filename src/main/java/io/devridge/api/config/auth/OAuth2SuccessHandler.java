package io.devridge.api.config.auth;

import io.devridge.api.domain.user.User;
import io.devridge.api.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static io.devridge.api.config.auth.OAuthSetting.OAUTH2_LOGIN_AFTER_PAGE;


@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final TokenService tokenService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        User user = loginUser.getUser();

        String accessToken = tokenService.createAccessToken(user);
        String refreshToken = tokenService.getOrUpdateRefreshToken(user);

        setRefreshTokenCookie(response, refreshToken);
        String targetUrl = getTargetUrl(accessToken);
        response.sendRedirect(targetUrl);
    }

    private String getTargetUrl(String accessToken) {
        return OAUTH2_LOGIN_AFTER_PAGE + "?accessToken=" + accessToken;
    }

    private void setRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        int expiresInDays = 7;
        int expiresInSeconds = expiresInDays * 24 * 60 * 60;
        ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
                .domain(".devridge.dev")
                .sameSite("None")
                .secure(true)
                .path("/")
                .httpOnly(true)
                .maxAge(expiresInSeconds)
                .build();
        response.addHeader("Set-Cookie", cookie.toString());
    }
}
