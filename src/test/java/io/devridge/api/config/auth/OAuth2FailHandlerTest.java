package io.devridge.api.config.auth;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static io.devridge.api.config.auth.OAuthSetting.OAUTH2_LOGIN_FAIL_PAGE;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OAuth2FailHandlerTest {

    @InjectMocks
    private OAuth2FailHandler oAuth2FailHandler;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @DisplayName("이메일과 SNS 제공자가 맞지않으면 로그인 실패 페이지로 이동하고 unmatched_email_and_provider 에러를 전달한다.")
    @Test
    public void oauth2_unmatched_email_and_provider_exception_test() throws IOException, ServletException {
        OAuth2Error oAuth2Error = new OAuth2Error("unmatched_email_and_provider");
        OAuth2AuthenticationException oAuth2AuthenticationException = new OAuth2AuthenticationException(oAuth2Error);

        oAuth2FailHandler.onAuthenticationFailure(request, response, oAuth2AuthenticationException);

        verify(response).sendRedirect(OAUTH2_LOGIN_FAIL_PAGE + "?error=unmatched_email_and_provider");
    }

    @DisplayName("지원하지 않는 SNS 제공자로 로그인을 시도하면 로그인 실패 페이지로 이동하고 unsupported_provider 에러를 전달한다.")
    @Test
    public void oauth2_unsupported_provider_exception_test() throws IOException, ServletException {
        OAuth2Error oAuth2Error = new OAuth2Error("unsupported_provider");
        OAuth2AuthenticationException oAuth2AuthenticationException = new OAuth2AuthenticationException(oAuth2Error);

        oAuth2FailHandler.onAuthenticationFailure(request, response, oAuth2AuthenticationException);

        verify(response).sendRedirect(OAUTH2_LOGIN_FAIL_PAGE + "?error=unsupported_provider");
    }

    @DisplayName("기타 예외 발생시 로그인 실패 페이지로 이동하고 server_error 에러를 전달한다.")
    @Test
    public void oauth2_another_error_exception_test() throws IOException, ServletException {
        OAuth2Error oAuth2Error = new OAuth2Error("some_other_error");
        OAuth2AuthenticationException oAuth2AuthenticationException = new OAuth2AuthenticationException(oAuth2Error);

        oAuth2FailHandler.onAuthenticationFailure(request, response, oAuth2AuthenticationException);

        verify(response).sendRedirect(OAUTH2_LOGIN_FAIL_PAGE + "?error=server_error");
    }
}