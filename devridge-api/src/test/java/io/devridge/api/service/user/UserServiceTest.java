package io.devridge.api.service.user;

import io.devridge.api.config.auth.OAuth2Attribute;
import io.devridge.api.handler.ex.UnmatchedEmailAndProviderException;
import io.devridge.api.repository.user.ApiUserRepository;
import io.devridge.api.service.user.UserService;
import io.devridge.core.domain.user.User;
import io.devridge.core.domain.user.UserRole;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private ApiUserRepository userRepository;

    @DisplayName("OAuth2 로그인을 통해 들어온 이메일을 가지고 유저 정보를 확인한다.")
    @Test
    public void find_sns_user() {
        // given
        String provider = "google";
        Map<String, Object> attributes = Map.of(
                "email", "test@test.com"
        );
        OAuth2Attribute oAuth2Attribute = OAuth2Attribute.of(provider, attributes);
        User testUser = User.builder().name("test").email("test@test.com").profilePicture("test.jpg").provider(provider).role(UserRole.USER).build();

        // stub
        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(testUser));

        // when
        User result = userService.findSnsUserOrRegisterIfNotExist(oAuth2Attribute);

        // then
        assertThat(result.getName()).isEqualTo("test");
        assertThat(result.getEmail()).isEqualTo("test@test.com");
        assertThat(result.getProfilePicture()).isEqualTo("test.jpg");
        assertThat(result.getProvider()).isEqualTo("google");
        assertThat(result.getRole()).isEqualTo(UserRole.USER);
    }

    @DisplayName("유저가 첫 로그인이면 회원가입을 진행한다.")
    @Test
    public void join_sns_user() {
        // given
        String provider = "google";
        Map<String, Object> attributes = Map.of(
                "email", "test@test.com",
                "name", "test1",
                "picture", "test1.jpg"
        );
        OAuth2Attribute oAuth2Attribute = OAuth2Attribute.of(provider, attributes);
        User user = User.builder().name("test").email("test@test.com").profilePicture("test.jpg").provider(provider).role(UserRole.USER).build();

        // stub
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        when(userRepository.save(userCaptor.capture())).thenReturn(user);

        // when
        User result = userService.findSnsUserOrRegisterIfNotExist(oAuth2Attribute);

        // then
        assertThat(result.getName()).isEqualTo("test");
        assertThat(result.getEmail()).isEqualTo("test@test.com");
        assertThat(result.getProfilePicture()).isEqualTo("test.jpg");
        assertThat(result.getProvider()).isEqualTo("google");
        assertThat(result.getRole()).isEqualTo(UserRole.USER);
    }

    @DisplayName("유저가 A SNS로 접근하였고 B SNS에 가입되어있는 경우 이메일이 동일하다면 회원가입을 할 수 없다.")
    @Test
    public void mismatch_email_and_provider() {
        // given
        String provider = "google";
        String anotherProvider = "wrongGoogle";
        Map<String, Object> attributes = Map.of(
                "email", "test@test.com",
                "name", "test1",
                "picture", "test1.jpg"
        );
        OAuth2Attribute oAuth2Attribute = OAuth2Attribute.of(provider, attributes);
        User user = User.builder().name("test").email("test@test.com").profilePicture("test.jpg").provider(anotherProvider).role(UserRole.USER).build();

        // stub
        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(user));

        // then
        assertThatThrownBy(() -> userService.findSnsUserOrRegisterIfNotExist(oAuth2Attribute))
                .isInstanceOf(UnmatchedEmailAndProviderException.class)
                .hasMessage("이메일과 소셜 로그인 정보가 일치하지 않습니다.");
    }
}