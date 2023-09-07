package io.devridge.api.config.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static io.devridge.api.config.auth.OAuthSetting.OAUTH2_LOGIN_FAIL_PAGE;

@Slf4j
@Component
public class OAuth2FailHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        log.error("Authentication failed: {}", exception.getMessage());

        if (exception instanceof OAuth2AuthenticationException) {
            OAuth2Error oauth2Error = ((OAuth2AuthenticationException) exception).getError();
            if ("unmatched_email_and_provider".equals(oauth2Error.getErrorCode())) {
                log.error("unmatched_email_and_provider: {}", oauth2Error.getDescription());
                response.sendRedirect(OAUTH2_LOGIN_FAIL_PAGE + "?error=unmatched_email_and_provider");
            } else {
                log.error("server_error: {}", oauth2Error.getDescription());
                response.sendRedirect(OAUTH2_LOGIN_FAIL_PAGE + "?error=server_error");
            }
        } else {
            response.sendRedirect(OAUTH2_LOGIN_FAIL_PAGE + "?error=server_error");
        }
    }
}
