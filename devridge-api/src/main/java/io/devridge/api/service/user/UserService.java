package io.devridge.api.service.user;

import io.devridge.api.config.auth.LoginUser;
import io.devridge.api.config.auth.OAuth2Attribute;
import io.devridge.api.dto.user.UserInfoForChannelTalkResponseDto;
import io.devridge.api.handler.ex.UnmatchedEmailAndProviderException;
import io.devridge.api.repository.user.ApiUserRepository;
import io.devridge.core.domain.user.User;
import io.devridge.core.domain.user.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@RequiredArgsConstructor
@Service
public class UserService {

    private final ApiUserRepository userRepository;

    @Transactional
    public User findSnsUserOrRegisterIfNotExist(OAuth2Attribute oAuth2Attribute) {
        return userRepository.findByEmail(oAuth2Attribute.getEmail())
                .map(user -> checkProviderAndReturnUser(user, oAuth2Attribute.getProvider()))
                .orElseGet(() -> registerUser(oAuth2Attribute));
    }

    public UserInfoForChannelTalkResponseDto getUserInfoForChannelTalk(LoginUser loginUser) {
        return new UserInfoForChannelTalkResponseDto(loginUser.getUser());
    }

    private User checkProviderAndReturnUser(User user, String provider) {
        if (!isMatchingProvider(user, provider)) {
            throw new UnmatchedEmailAndProviderException();
        }
        return user;
    }

    private boolean isMatchingProvider (User user, String provider) {
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
                .role(UserRole.USER)
                .build();
    }
}
