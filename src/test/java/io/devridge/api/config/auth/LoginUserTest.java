package io.devridge.api.config.auth;

import io.devridge.api.domain.user.User;
import io.devridge.api.domain.user.UserRole;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

class LoginUserTest {

    @DisplayName("로그인 한 사용자의 권한을 확인한다.")
    @Test
    public void oauth2_unmatched_email_and_provider_exception_test() throws IOException, ServletException {
        // given
        User user = User.builder().email("test@test.com").role(UserRole.USER).build();
        LoginUser loginUser = LoginUser.builder().user(user).build();

        // when
        Collection<? extends GrantedAuthority> authorities = loginUser.getAuthorities();
        String authority = authorities.iterator().next().getAuthority();

        // then
        assertThat(authorities).size().isEqualTo(1);
        assertThat(authority).isEqualTo(UserRole.USER.getKey());
    }
}