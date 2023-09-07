package io.devridge.api.config.auth;

import io.devridge.api.domain.user.User;
import io.devridge.api.service.TokenService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.RedirectStrategy;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static io.devridge.api.config.auth.OAuthSetting.OAUTH2_LOGIN_AFTER_PAGE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OAuth2SuccessHandlerTest {

    @InjectMocks
    private OAuth2SuccessHandler oAuth2SuccessHandler;

    @Mock
    private TokenService tokenService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private Authentication authentication;

    @Mock
    private LoginUser loginUser;

    @Mock
    private User user;

    @Mock
    private RedirectStrategy redirectStrategy;

    @Captor
    private ArgumentCaptor<Cookie> cookieCaptor;

    @DisplayName("OAuth2 로그인에 성공하면 쿠키에 refreshToken을 저장하고 url 뒤에 accessToken을 붙여서 리다이렉트 한다.")
    @Test
    public void onAuthenticationSuccess_test() throws IOException {
        // stub
        oAuth2SuccessHandler.setRedirectStrategy(redirectStrategy);
        when(authentication.getPrincipal()).thenReturn(loginUser);
        when(loginUser.getUser()).thenReturn(user);
        when(tokenService.createAccessToken(user)).thenReturn("accessTestToken");
        when(tokenService.createRefreshTokenAndSave(user)).thenReturn("refreshTestToken");

        // when
        oAuth2SuccessHandler.onAuthenticationSuccess(request, response, authentication);

        // then
        verify(response).addCookie(cookieCaptor.capture());

        Cookie actualCookie = cookieCaptor.getValue();
        assertEquals("refreshToken", actualCookie.getName());
        assertEquals("refreshTestToken", actualCookie.getValue());

        verify(redirectStrategy).sendRedirect(eq(request), eq(response), eq(OAUTH2_LOGIN_AFTER_PAGE + "?accessToken=accessTestToken"));
    }
}