package io.devridge.api.util.jwt;

import io.devridge.api.mock.FakeTimeProvider;
import io.devridge.api.mock.FakeTokenProvider;
import io.devridge.api.util.jwt.exception.JwtExpiredException;
import io.devridge.api.util.jwt.exception.JwtIdConversionException;
import io.devridge.api.util.jwt.exception.JwtNotHaveIdException;
import io.devridge.api.util.jwt.exception.JwtVerifyException;
import io.devridge.core.domain.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

class TokenProcessTest {

    private TokenProcess tokenProcess;
    private FakeTokenProvider tokenProvider;
    private FakeTimeProvider timeProvider;

    @BeforeEach
    public void setUp() {
        timeProvider = new FakeTimeProvider();
        tokenProvider = new FakeTokenProvider();
        tokenProcess = new TokenProcess(timeProvider, tokenProvider);
    }

    @DisplayName("Access 토큰을 생성하고 토큰과 만료일을 반환한다.")
    @Test
    public void create_access_token() {
        // given
        User user = User.builder().id(1L).email("test@test.com").build();
        tokenProvider.token = "success_token";
        timeProvider.currentTime = LocalDateTime.of(2020, 1, 1, 0, 0, 0);

        // when
        TokenDto result = tokenProcess.createAccessToken(user);

        // then
        assertThat(result.getTokenContent()).isEqualTo("access_success_token");
        assertThat(result.getExpiredAt()).isEqualTo(LocalDateTime.of(2020, 1, 1, 0, 30, 0));
    }

    @DisplayName("Refresh 토큰을 생성하고 토큰과 만료일을 반환한다.")
    @Test
    public void create_refresh_token() {
        // given
        tokenProvider.token = "success_token";
        timeProvider.currentTime = LocalDateTime.of(2020, 1, 1, 0, 0, 0);

        // when
        TokenDto result = tokenProcess.createRefreshToken();

        // then
        assertThat(result.getTokenContent()).isEqualTo("refresh_success_token");
        assertThat(result.getExpiredAt()).isEqualTo(LocalDateTime.of(2020, 1, 8, 0, 0, 0));
    }

    @DisplayName("토큰 검증에 실패할 때 예외를 발생시킨다.")
    @Test
    public void verify_and_get_user_id_token_verification_fail_test() {
        // given
        tokenProvider.token = "another_token";

        // when & then
        assertThatThrownBy(() -> tokenProcess.verifyAndGetUserId("invalid_token"))
                .isInstanceOf(JwtVerifyException.class)
                .hasMessage("검증에 실패하였습니다.");
    }

    @DisplayName("토큰이 만료되었을 때 예외를 발생시킨다.")
    @Test
    public void verify_and_get_user_id_token_expired_fail_test() {
        // given
        tokenProvider.token = "expired_token";
        tokenProvider.currentAt = LocalDateTime.of(2020, 1, 1, 0, 0, 1);
        tokenProvider.expiredAt = LocalDateTime.of(2020, 1, 1, 0, 0, 0);

        // when & then
        assertThatThrownBy(() -> tokenProcess.verifyAndGetUserId("expired_token"))
                .isInstanceOf(JwtExpiredException.class)
                .hasMessage("토큰이 만료되었습니다.");
    }

    @DisplayName("토큰에 userId가 없을 때 예외를 발생시킨다.")
    @Test
    public void verify_and_get_user_id_null_id() {
        // given
        tokenProvider.token = "token_without_id";
        tokenProvider.userId = null;

        // when & then
        assertThatThrownBy(() -> tokenProcess.verifyAndGetUserId("token_without_id"))
                .isInstanceOf(JwtNotHaveIdException.class);
    }

    @DisplayName("토큰의 userId가 Long 값으로 변환할 수 없을 때 예외를 발생시킨다.")
    @Test
    public void verify_and_get_user_id_conversion_error() {
        // given
        tokenProvider.token = "token_with_invalid_id";
        tokenProvider.userId = "invalid_id";

        // when & then
        assertThatThrownBy(() -> tokenProcess.verifyAndGetUserId("token_with_invalid_id"))
                .isInstanceOf(JwtIdConversionException.class)
                .hasMessage("long 값으로 변환할 수 없습니다.");
    }

    @DisplayName("토큰 검증이 성공하면 userId를 반환한다.")
    @Test
    public void verify_and_get_user_id_success() {
        // given
        tokenProvider.token = "success_token";
        tokenProvider.userId = "1";

        // when
        Long userId = tokenProcess.verifyAndGetUserId("success_token");

        // then
        assertThat(userId).isEqualTo(1L);
    }

    @DisplayName("토큰이 유효하고 만료되지 않았을 때 true를 반환한다.")
    @Test
    public void token_is_valid_success_test() {
        //given
        tokenProvider.token = "success_token";
        tokenProvider.currentAt = LocalDateTime.of(2020, 1, 1, 0, 0, 0);
        tokenProvider.expiredAt = LocalDateTime.of(2020, 1, 1, 0, 0, 1);

        // when
        boolean result = tokenProcess.isTokenValid("success_token");

        // then
        assertThat(result).isTrue();
    }

    @DisplayName("토큰이 유효하지 않을 때 false를 반환한다.")
    @Test
    public void token_is_valid_fail_test() {
        //given
        tokenProvider.token = "wrong_token";

        // when
        boolean result = tokenProcess.isTokenValid("success_token");

        // then
        assertThat(result).isFalse();
    }

    @DisplayName("토큰이 만료되었을 때 false를 반환한다.")
    @Test
    public void token_is_expired_fail_test() {
        // given
        tokenProvider.token = "success_token";
        tokenProvider.currentAt = LocalDateTime.of(2020, 1, 1, 0, 0, 1);
        tokenProvider.expiredAt = LocalDateTime.of(2020, 1, 1, 0, 0, 0);

        // when
        boolean result = tokenProcess.isTokenValid("success_token");

        // then
        assertThat(result).isFalse();
    }

    @DisplayName("토큰 검증에 성공하면 아무 일도 일어나지 않는다.")
    @Test
    public void token_valid_or_throw_exception_success_test() {
        // given
        tokenProvider.token = "success_token";
        tokenProvider.currentAt = LocalDateTime.of(2020, 1, 1, 0, 0, 0);
        tokenProvider.expiredAt = LocalDateTime.of(2020, 1, 1, 0, 0, 1);

        // when & then
        assertThatCode(() -> tokenProcess.tokenValidOrThrowException("success_token"))
                .doesNotThrowAnyException();
    }

    @DisplayName("토큰 검증에 실패하면 JwtVerifyException을 발생시킨다.")
    @Test
    public void token_verify_fail_test() {
        // given
        tokenProvider.token = "success_token";

        // when & then
        assertThatThrownBy(() -> tokenProcess.tokenValidOrThrowException("fail_token"))
                .isInstanceOf(JwtVerifyException.class);
    }

    @DisplayName("토큰이 만료되면 JwtExpiredException을 발생시킨다.")
    @Test
    public void token_expired_fail_test() {
        // given
        tokenProvider.token = "success_token";
        tokenProvider.currentAt = LocalDateTime.of(2020, 1, 1, 0, 0, 1);
        tokenProvider.expiredAt = LocalDateTime.of(2020, 1, 1, 0, 0, 0);

        // when & then
        assertThatThrownBy(() -> tokenProcess.tokenValidOrThrowException("success_token"))
                .isInstanceOf(JwtExpiredException.class);
    }
}