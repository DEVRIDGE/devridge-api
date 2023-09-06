package io.devridge.api.config.auth;

import io.devridge.api.domain.user.User;
import io.devridge.api.domain.user.UserRepository;
import io.devridge.api.domain.user.UserRole;
import io.devridge.api.handler.ex.UnmatchedEmailAndProviderException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@RequiredArgsConstructor
@Transactional
@Service
public class OAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = loadUserFromSuper(userRequest);
        OAuth2Attribute oAuth2Attribute =
                OAuth2Attribute.of(userRequest, oAuth2User.getAttributes());

        User user = findUserOrRegister(oAuth2Attribute);

        return createOAuth2User(user, oAuth2Attribute);
    }

    protected OAuth2User loadUserFromSuper(OAuth2UserRequest userRequest) {
        return super.loadUser(userRequest);
    }

    private User findUserOrRegister(OAuth2Attribute oAuth2Attribute) {
        return userRepository.findByEmail(oAuth2Attribute.getEmail())
                .map(user -> checkProviderAndReturnUser(user, oAuth2Attribute.getProvider()))
                .orElseGet(() -> registerUser(oAuth2Attribute));
    }

    private User checkProviderAndReturnUser(User user, String provider) {
        if (!doesProviderMatch(user, provider)) {
            throw new UnmatchedEmailAndProviderException();
        }
        return user;
    }

    private boolean doesProviderMatch(User user, String provider) {
        return Objects.equals(user.getProvider(), provider);
    }

    private User registerUser(OAuth2Attribute oAuth2Attribute) {
        return userRepository.save(createUser(oAuth2Attribute));
    }

    private User createUser(OAuth2Attribute oAuth2Attribute) {
        return User.builder()
                .name(oAuth2Attribute.getName())
                .email(oAuth2Attribute.getEmail())
                .profilePicture(oAuth2Attribute.getPicture())
                .provider(oAuth2Attribute.getProvider())
                .providerId(oAuth2Attribute.getAttributes().get(oAuth2Attribute.getProviderIdKey()).toString())
                .role(UserRole.USER)
                .build();
    }

    private OAuth2User createOAuth2User(User user, OAuth2Attribute oAuth2User) {
        return LoginUser.builder()
                .user(user)
                .attributes(oAuth2User.getAttributes())
                .build();
    }
}
