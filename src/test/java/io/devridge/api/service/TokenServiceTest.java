package io.devridge.api.service;

import io.devridge.api.domain.token.Token;
import io.devridge.api.domain.token.TokenRepository;
import io.devridge.api.domain.user.User;
import io.devridge.api.domain.user.UserRole;
import io.devridge.api.dto.token.ReissueTokenRequestDto;
import io.devridge.api.dto.token.ReissueTokenResponseDto;
import io.devridge.api.handler.ex.NotHaveRefreshTokenException;
import io.devridge.api.util.jwt.TokenDto;
import io.devridge.api.util.jwt.TokenProcess;
import io.devridge.api.util.jwt.exception.JwtExpiredException;
import io.devridge.api.util.jwt.exception.JwtVerifyException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TokenServiceTest {

    @InjectMocks
    private TokenService tokenService;

    @Mock
    private TokenProcess tokenProcess;

    @Mock
    private TokenRepository tokenRepository;

    @DisplayName("로그인에 필요한 access 토큰을 생성한다.")
    @Test
    public void create_access_token_test() {
        // given
        User user = User.builder().email("test@test.com").role(UserRole.USER).build();
        TokenDto tokenDto = TokenDto.builder().token("new_access_token").build();

        // stub
        when(tokenProcess.createAccessToken(user)).thenReturn(tokenDto);

        // when
        String result = tokenService.createAccessToken(user);

        // then
        assertThat(result).isEqualTo("new_access_token");
    }

    @DisplayName("해당 유저의 refresh Token이 존재하지 않으면 새로운 refresh Token을 생성하고 저장한다.")
    @Test
    public void create_refresh_token_test() {
        // given
        User user = User.builder().email("test@test.com").role(UserRole.USER).build();
        TokenDto tokenDto = TokenDto.builder().token("refresh_test_token").build();

        // stub
        when(tokenProcess.createRefreshToken()).thenReturn(tokenDto);
        when(tokenRepository.findByUser(user)).thenReturn(Optional.empty());
        ArgumentCaptor<Token> tokenCaptor = ArgumentCaptor.forClass(Token.class);
        when(tokenRepository.save(tokenCaptor.capture())).thenReturn(Token.builder().id(1L).content(tokenDto.getToken()).expiredAt(tokenDto.getExpiredAt()).build());

        // when
        String result = tokenService.getOrUpdateRefreshToken(user);

        // then
        assertThat(result).isEqualTo("refresh_test_token");
    }

    @DisplayName("해당 유저의 refresh Token이 존재하지만 유효하지 않거나 만료되었으면 새로운 refresh Token으로 수정한다.")
    @Test
    public void update_refresh_token_test() {
        // given
        User user = User.builder().id(1L).email("test@test.com").role(UserRole.USER).build();
        Token token = Token.builder().id(1L).content("old_refresh_token").user(user).build();
        TokenDto tokenDto = TokenDto.builder().token("new_refresh_token").build();

        // stub
        when(tokenProcess.createRefreshToken()).thenReturn(tokenDto);
        when(tokenRepository.findByUser(user)).thenReturn(Optional.ofNullable(token));
        when(tokenProcess.isTokenValid(token.getContent())).thenReturn(false);

        // when
        String result = tokenService.getOrUpdateRefreshToken(user);

        // then
        assertThat(result).isEqualTo("new_refresh_token");
    }

    @DisplayName("해당 유저의 refresh Token이 존재하고 유효하면 기존의 refresh Token을 응답한다.")
    @Test
    public void send_old_refresh_token_test() {
        // given
        User user = User.builder().id(1L).email("test@test.com").role(UserRole.USER).build();
        Token token = Token.builder().id(1L).content("old_refresh_token").user(user).build();

        // stub
        when(tokenRepository.findByUser(user)).thenReturn(Optional.ofNullable(token));
        when(tokenProcess.isTokenValid(token.getContent())).thenReturn(true);

        // when
        String result = tokenService.getOrUpdateRefreshToken(user);

        // then
        assertThat(result).isEqualTo("old_refresh_token");
    }

    @DisplayName("정상적인 refresh token으로 access token을 재발행한다.")
    @Test
    public void reissue_access_token_having_correct_refresh_token() {
        // given
        ReissueTokenRequestDto reissueTokenRequestDto = ReissueTokenRequestDto.builder().token("success_refresh_token").build();
        User user = User.builder().email("test@test.com").role(UserRole.USER).build();
        Token oldRefreshToken = Token.builder().content("old_refresh_token").user(user).build();
        TokenDto tokenDto = TokenDto.builder().token("new_access_token").expiredAt(LocalDateTime.of(2000, 1, 1, 0, 0, 0)).build();

        // stub
        when(tokenRepository.findByContent(any())).thenReturn(Optional.of(oldRefreshToken));
        when(tokenProcess.createAccessToken(any())).thenReturn(tokenDto);

        // when
        ReissueTokenResponseDto reissueTokenResponseDto = tokenService.reissue(reissueTokenRequestDto);

        // then
        assertThat(reissueTokenResponseDto.getAccessToken()).isEqualTo("new_access_token");
        assertThat(reissueTokenResponseDto.getTokenType()).isEqualTo("Bearer");
        assertThat(reissueTokenResponseDto.getExpiresIn()).isEqualTo(1800);
        assertThat(reissueTokenResponseDto.getExpiredAt()).isEqualTo(LocalDateTime.of(2000, 1, 1, 0, 0, 0));
    }

    @DisplayName("refresh token을 저장소에서 찾을 수 없을 때 예외를 발생시킨다.")
    @Test
    public void found_token_fail_test() {
        // given
        ReissueTokenRequestDto reissueTokenRequestDto = ReissueTokenRequestDto.builder().token("fail_refresh_token").build();

        // stub
        when(tokenRepository.findByContent(any())).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> tokenService.reissue(reissueTokenRequestDto))
                .isInstanceOf(NotHaveRefreshTokenException.class);
    }

    @DisplayName("refresh token 검증에 실패하면 예외를 발생시킨다.")
    @Test
    public void verify_token_fail_test() {
        // given
        ReissueTokenRequestDto reissueTokenRequestDto = ReissueTokenRequestDto.builder().token("old_refresh_token").build();
        User user = User.builder().email("test@test.com").role(UserRole.USER).build();
        Token oldRefreshToken = Token.builder().content("old_refresh_token").user(user).build();

        // stub
        when(tokenRepository.findByContent(any())).thenReturn(Optional.of(oldRefreshToken));
        doThrow(new JwtVerifyException("토큰 검증 실패")).when(tokenProcess).tokenValidOrThrowException("old_refresh_token");

        // when & then
        assertThatThrownBy(() -> tokenService.reissue(reissueTokenRequestDto))
                .isInstanceOf(JwtVerifyException.class);
    }

    @DisplayName("refresh token이 만료되었으면 예외를 발생시킨다.")
    @Test
    public void expired_token_fail_test() {
        // given
        ReissueTokenRequestDto reissueTokenRequestDto = ReissueTokenRequestDto.builder().token("old_refresh_token").build();
        User user = User.builder().email("test@test.com").role(UserRole.USER).build();
        Token oldRefreshToken = Token.builder().content("old_refresh_token").user(user).build();

        // stub
        when(tokenRepository.findByContent(any())).thenReturn(Optional.of(oldRefreshToken));
        doThrow(new JwtExpiredException("토큰 만료")).when(tokenProcess).tokenValidOrThrowException("old_refresh_token");

        // when & then
        assertThatThrownBy(() -> tokenService.reissue(reissueTokenRequestDto))
                .isInstanceOf(JwtExpiredException.class);
    }
}