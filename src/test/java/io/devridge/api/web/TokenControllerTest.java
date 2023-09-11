package io.devridge.api.web;

import io.devridge.api.domain.token.Token;
import io.devridge.api.domain.token.TokenRepository;
import io.devridge.api.util.jwt.TokenDto;
import io.devridge.api.util.jwt.TokenProcess;
import io.devridge.api.util.jwt.exception.JwtExpiredException;
import io.devridge.api.util.jwt.exception.JwtVerifyException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class TokenControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private TokenRepository tokenRepository;

    @MockBean
    private TokenProcess tokenProcess;

    @DisplayName("Refresh Token으로 재발행을 요청하면 Access Token을 재발행 후 정상적으로 응답한다.")
    @Test
    public void token_reissue_success_test() throws Exception {
        // given
        Token token = Token.builder().id(1L).content("correctToken").build();
        TokenDto tokenDto = TokenDto.builder().token("newAccessToken").expiredAt(LocalDateTime.of(2000, 1, 1, 0, 0, 0)).build();

        // stub
        when(tokenRepository.findByContent(any())).thenReturn(Optional.of(token));
        when(tokenProcess.createAccessToken(any())).thenReturn(tokenDto);

        // when
        ResultActions resultActions = mvc.perform(
                post("/token/reissue")
                        .param("token", "correctToken")
        );

        // then
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.status").value("success"));
        resultActions.andExpect(jsonPath("$.message").value("발급 완료"));
        resultActions.andExpect(jsonPath("$.data.accessToken").value("newAccessToken"));
        resultActions.andExpect(jsonPath("$.data.tokenType").value("Bearer"));
        resultActions.andExpect(jsonPath("$.data.expiresIn").value(1800));
    }

    @DisplayName("토큰 재발행 API에서 잘못된 형식으로 보내면 에러를 응답한다.")
    @ParameterizedTest
    @MethodSource("invalidKeys")
    public void send_wrong_form_fail_test(String key) throws Exception {
        // given & when
        ResultActions resultActions = mvc.perform(
                post("/token/reissue")
                        .param(key, "correctToken")
        );

        // then
        resultActions.andExpect(status().isBadRequest());
        resultActions.andExpect(jsonPath("$.status").value("error"));
        resultActions.andExpect(jsonPath("$.message").value("잘못된 형식 요청"));
        resultActions.andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("Refresh Token을 저장소에서 찾을 수 없으면 에러를 응답한다.")
    @Test
    public void token_reissue_token_found_fail_test() throws Exception {
        // stub
        when(tokenRepository.findByContent(any())).thenReturn(Optional.empty());

        // when
        ResultActions resultActions = mvc.perform(
                post("/token/reissue")
                        .param("token", "correctToken")
        );

        // then
        resultActions.andExpect(status().isBadRequest());
        resultActions.andExpect(jsonPath("$.status").value("error"));
        resultActions.andExpect(jsonPath("$.message").value("토큰 오류"));
        resultActions.andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("Refresh Token을 검증할 수 없으면 에러를 응답한다.")
    @Test
    public void token_reissue_token_valid_fail_test() throws Exception {
        // given
        Token token = Token.builder().id(1L).content("correctToken").build();

        // stub
        when(tokenRepository.findByContent(any())).thenReturn(Optional.of(token));
        doThrow(new JwtVerifyException("토큰 검증 오류"))
                .when(tokenProcess).tokenValidOrThrowException(any());

        // when
        ResultActions resultActions = mvc.perform(
                post("/token/reissue")
                        .param("token", "correctToken")
        );

        // then
        resultActions.andExpect(status().isBadRequest());
        resultActions.andExpect(jsonPath("$.status").value("error"));
        resultActions.andExpect(jsonPath("$.message").value("토큰 오류"));
        resultActions.andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("Refresh Token이 만료되었으면 에러를 응답한다.")
    @Test
    public void token_reissue_token_expired_fail_test() throws Exception {
        // given
        Token token = Token.builder().id(1L).content("correctToken").build();

        // stub
        when(tokenRepository.findByContent(any())).thenReturn(Optional.of(token));
        doThrow(new JwtExpiredException("토큰 만료 오류"))
                .when(tokenProcess).tokenValidOrThrowException(any());

        // when
        ResultActions resultActions = mvc.perform(
                post("/token/reissue")
                        .param("token", "correctToken")
        );

        // then
        resultActions.andExpect(status().isBadRequest());
        resultActions.andExpect(jsonPath("$.status").value("error"));
        resultActions.andExpect(jsonPath("$.message").value("토큰 만료"));
        resultActions.andExpect(jsonPath("$.data").isEmpty());
    }

    private static Stream<String> invalidKeys() {
        return Stream.of(
                " " + "token",
                "token" + " ",
                " ",
                "tokenWrong"
        );
    }
}