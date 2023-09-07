package io.devridge.api.config.auth;

import io.devridge.api.domain.user.User;
import io.devridge.api.handler.ex.UnmatchedEmailAndProviderException;
import io.devridge.api.handler.ex.UnsupportedProviderException;
import io.devridge.api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OAuth2UserService extends DefaultOAuth2UserService {

    private final UserService userService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        try {
            OAuth2User oAuth2User = loadUserFromSuper(userRequest);
            String oauthProvider = userRequest.getClientRegistration().getRegistrationId();

            OAuth2Attribute oAuth2Attribute = OAuth2Attribute.of(oauthProvider , oAuth2User.getAttributes());
            User user = userService.findSnsUserOrRegisterIfNotExist(oAuth2Attribute);

            return createOAuth2User(user, oAuth2Attribute);
        } catch (UnmatchedEmailAndProviderException exception) {
            OAuth2Error oauth2Error = new OAuth2Error("unmatched_email_and_provider", exception.getMessage(), null);
            throw new OAuth2AuthenticationException(oauth2Error, exception.getMessage());
        } catch (UnsupportedProviderException exception) {
            OAuth2Error oauth2Error = new OAuth2Error("unsupported_provider", exception.getMessage(), null);
            throw new OAuth2AuthenticationException(oauth2Error, exception.getMessage());
        }
    }

    protected OAuth2User loadUserFromSuper(OAuth2UserRequest userRequest) {
        return super.loadUser(userRequest);
    }

    private OAuth2User createOAuth2User(User user, OAuth2Attribute oAuth2User) {
        return LoginUser.builder()
                .user(user)
                .attributes(oAuth2User.getAttributes())
                .build();
    }
}
