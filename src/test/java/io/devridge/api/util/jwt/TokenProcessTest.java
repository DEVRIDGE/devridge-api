package io.devridge.api.util.jwt;

import io.devridge.api.domain.user.User;
import io.devridge.api.mock.FakeTimeProvider;
import io.devridge.api.mock.FakeTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class TokenProcessTest {

    private TokenProcess tokenProcess;

    @BeforeEach
    public void setUp() {
        FakeTimeProvider fakeTimeProvider = new FakeTimeProvider(LocalDateTime.of(2023, 9, 7, 13, 20, 30));
        FakeTokenProvider tokenProvider = new FakeTokenProvider("success_token");
        tokenProcess = new TokenProcess(fakeTimeProvider, tokenProvider);
    }


    @DisplayName("Access 토큰을 생성하고 토큰과 만료일을 반환한다.")
    @Test
    public void create_access_token() {
        // given
        User user = User.builder().id(1L).email("test@test.com").build();

        // when
        TokenDto result = tokenProcess.createAccessToken(user);

        // then
        assertThat(result.getToken()).isEqualTo("access_success_token");
        assertThat(result.getExpiredAt()).isEqualTo(LocalDateTime.of(2023, 9, 7, 13, 50, 30));
    }

    @DisplayName("Refresh 토큰을 생성하고 토큰과 만료일을 반환한다.")
    @Test
    public void create_refresh_token() {
        // when
        TokenDto result = tokenProcess.createRefreshToken();

        // then
        assertThat(result.getToken()).isEqualTo("refresh_success_token");
        assertThat(result.getExpiredAt()).isEqualTo(LocalDateTime.of(2023, 9, 14, 13, 20, 30));
    }
}