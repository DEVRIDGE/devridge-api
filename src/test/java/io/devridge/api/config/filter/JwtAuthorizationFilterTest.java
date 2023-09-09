package io.devridge.api.config.filter;

import io.devridge.api.domain.user.User;
import io.devridge.api.domain.user.UserRepository;
import io.devridge.api.domain.user.UserRole;
import io.devridge.api.mock.FakeJwtConfiguration;
import io.devridge.api.mock.FakeTokenProvider;
import io.devridge.api.util.jwt.JwtSetting;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Stream;

import static io.devridge.api.util.jwt.JwtSetting.TOKEN_PREFIX;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@Import({ FakeJwtConfiguration.class })
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class JwtAuthorizationFilterTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FakeTokenProvider tokenProvider;

    @MockBean
    private UserRepository userRepository;

    @BeforeEach
    public void init() {
        tokenProvider.init();
    }

    @DisplayName("로그인이 필요없는 API에 토큰을 가지고 있어도 검증 없이 응답한다.")
    @Test
    public void permit_all_page_not_valid_token_success_test() throws Exception {
        // given
        String permitAllUrl = "/test";

        // when
        ResultActions result = mockMvc.perform(get(permitAllUrl)
                .header(JwtSetting.JWT_HEADER, TOKEN_PREFIX + "test"));

        // then
        result.andExpect(status().isNotFound());
    }

    @DisplayName("로그인 여부에 따라 정보가 달라지는 API에 로그인 안한 사용자가 요청해도 정상적으로 응답한다.")
    @ParameterizedTest
    @ValueSource(strings = { "/optional_login/test", "/optional_login/wild_test/1", "/optional_login/1/wild_test", "/1/optional_login/wild_test" })
    public void not_login_user_basic_info_api_success_test(String optionalUrl) throws Exception {
        // when
        ResultActions result = mockMvc.perform(get(optionalUrl));

        // then
        result.andExpect(status().isNotFound());
    }

    @DisplayName("로그인 여부에 따라 정보가 달라지는 API에 인증 형식을 잘못 보내면 로그인 안한 유저로 취급하고 정상적으로 응답한다.")
    @ParameterizedTest
    @MethodSource("headerValuesProvider")
    public void not_valid_header_is_not_login_user_api_success_test(String headerKey, String headerValue) throws Exception {
        // given
        String optionalUrl = "/optional_login/test";
        tokenProvider.token = "correctToken";

        // when
        ResultActions result = mockMvc.perform(get(optionalUrl)
                .header(headerKey, headerValue));

        // then
        result.andExpect(status().isNotFound());
    }

    @DisplayName("잘못된 형식의 토큰을 보내면 에러를 응답한다")
    @ParameterizedTest
    @MethodSource("verificationFailureTokenProvider")
    public void token_verification_fail_test(String token) throws Exception {
        // given
        String optionalUrl = "/optional_login/test";
        tokenProvider.token = "correctToken";

        // when
        ResultActions result = mockMvc.perform(get(optionalUrl)
                .header(JwtSetting.JWT_HEADER, token));

        // then
        result.andExpect(status().isUnauthorized());
        result.andExpect(jsonPath("$.status").value("error"));
        result.andExpect(jsonPath("$.message").value("토큰 검증 오류"));
        result.andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("토큰 만료되면 에러를 응답한다")
    @Test
    public void token_expired_fail_test() throws Exception {
        // given
        String optionalUrl = "/optional_login/test";
        tokenProvider.token = "correctToken";
        tokenProvider.currentAt = LocalDateTime.of(2020, 1, 1, 0, 0, 1);
        tokenProvider.expiredAt = LocalDateTime.of(2020, 1, 1, 0, 0, 0);

        // when
        ResultActions result = mockMvc.perform(get(optionalUrl)
                .header(JwtSetting.JWT_HEADER, TOKEN_PREFIX + "correctToken"));

        // then
        result.andExpect(status().isUnauthorized());
        result.andExpect(jsonPath("$.status").value("error"));
        result.andExpect(jsonPath("$.message").value("토큰 만료"));
        result.andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("토큰 해독은 정상적으로 되었지만 user id가 없으면 에러를 응답한다")
    @Test
    public void token_not_have_user_id_fail_test() throws Exception {
        // given
        String optionalUrl = "/optional_login/test";
        tokenProvider.token = "correctToken";

        // when
        ResultActions result = mockMvc.perform(get(optionalUrl)
                .header(JwtSetting.JWT_HEADER, TOKEN_PREFIX + "correctToken"));

        // then
        result.andExpect(status().isUnauthorized());
        result.andExpect(jsonPath("$.status").value("error"));
        result.andExpect(jsonPath("$.message").value("토큰 검증 오류"));
        result.andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("토큰에 user_id 값이 있지만 long값이 아닌 경우 에러를 응답한다.")
    @Test
    public void token_id_convert_long_fail_test() throws Exception {
        // given
        String optionalUrl = "/optional_login/test";
        tokenProvider.token = "correctToken";
        tokenProvider.userId = "FAKE";

        // when
        ResultActions result = mockMvc.perform(get(optionalUrl)
                .header(JwtSetting.JWT_HEADER, TOKEN_PREFIX + "correctToken"));

        // then
        result.andExpect(status().isUnauthorized());
        result.andExpect(jsonPath("$.status").value("error"));
        result.andExpect(jsonPath("$.message").value("토큰 검증 오류"));
        result.andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("토큰이 제대로 검증되었지만 DB에서 유저를 찾을 수 없는 경우 에러를 응답한다.")
    @Test
    public void not_found_user_id_fail_test() throws Exception {
        // given
        String optionalUrl = "/optional_login/test";
        tokenProvider.token = "correctToken";
        tokenProvider.userId = "1";

        // when
        ResultActions result = mockMvc.perform(get(optionalUrl)
                .header(JwtSetting.JWT_HEADER, TOKEN_PREFIX + "correctToken"));

        // then
        result.andExpect(status().isUnauthorized());
        result.andExpect(jsonPath("$.status").value("error"));
        result.andExpect(jsonPath("$.message").value("토큰 검증 오류"));
        result.andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("토큰이 정상적으로 검증되고 유저를 찾을 수 있으면 성공을 응답한다.")
    @Test
    public void verify_token_success_test() throws Exception {
        // given
        String optionalUrl = "/optional_login/test";
        tokenProvider.token = "correctToken";
        tokenProvider.userId = "1";

        when(userRepository.findById(any())).thenReturn(Optional.of(User.builder().id(1L).email("test@test.com").role(UserRole.USER).build()));

        // when
        ResultActions result = mockMvc.perform(get(optionalUrl)
                .header(JwtSetting.JWT_HEADER, TOKEN_PREFIX + "correctToken"));

        // then
        result.andExpect(status().isNotFound());
    }

    private static Stream<Arguments> headerValuesProvider() {
        return Stream.of(
                arguments(JwtSetting.JWT_HEADER + " ", "correctToken"),
                arguments(" " + JwtSetting.JWT_HEADER, "correctToken"),
                arguments(" ", "correctToken"),
                arguments("wrongHeader", "correctToken"),
                arguments(JwtSetting.JWT_HEADER, "")
        );
    }

    private static Stream<Arguments> verificationFailureTokenProvider() {
        return Stream.of(
                arguments(TOKEN_PREFIX),
                arguments(TOKEN_PREFIX + " "),
                arguments("Bearer" + "correctToken"),
                arguments(TOKEN_PREFIX + "wrongToken")
        );
    }
}