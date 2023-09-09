package io.devridge.api.service;

import io.devridge.api.domain.token.Token;
import io.devridge.api.domain.token.TokenRepository;
import io.devridge.api.domain.user.User;
import io.devridge.api.domain.user.UserRole;
import io.devridge.api.util.jwt.TokenDto;
import io.devridge.api.util.jwt.TokenProcess;
import io.devridge.api.util.time.TimeProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TokenServiceTest {

    @InjectMocks
    private TokenService tokenService;

    @Mock
    private TokenProcess tokenProcess;

    @Mock
    private TimeProvider timeProvider;

    @Mock
    private TokenRepository tokenRepository;

    @DisplayName("로그인에 필요한 access 토큰을 생성한다.")
    @Test
    public void create_access_token_test() {
        // given
        User user = User.builder().email("test@test.com").role(UserRole.USER).build();
        TokenDto tokenDto = TokenDto.builder().token("access_test_token").build();

        // stub
        when(tokenProcess.createAccessToken(user)).thenReturn(tokenDto);

        // when
        String result = tokenService.createAccessToken(user);

        // then
        assertThat(result).isEqualTo("access_test_token");
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
}