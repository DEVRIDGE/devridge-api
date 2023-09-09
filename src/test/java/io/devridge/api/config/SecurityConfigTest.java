package io.devridge.api.config;

import io.devridge.api.util.jwt.JwtSetting;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @DisplayName("로그인이 필요없는 API는 로그인이 없어도 정상적으로 응답한다.")
    @Test
    public void permit_all_page_success_test() throws Exception {
        // given
        String permitAllUrl = "/test";

        // when
        ResultActions result = mockMvc.perform(get(permitAllUrl));

        // then
        result.andExpect(status().isNotFound());
    }

    @DisplayName("로그인이 필요없는 API에 토큰을 가지고 있어서 검증 없이 응답한다.")
    @Test
    public void permit_all_page_not_valid_token_success_test() throws Exception {
        // given
        String permitAllUrl = "/test";

        // when
        ResultActions result = mockMvc.perform(get(permitAllUrl)
                .header(JwtSetting.JWT_HEADER, JwtSetting.TOKEN_PREFIX + "test"));

        // then
        result.andExpect(status().isNotFound());
    }

    @DisplayName("로그인 필수 API에 로그인이 되어있으면 정상적으로 응답한다.")
    @WithMockUser(username="test")
    @ParameterizedTest
    @ValueSource(strings = { "/required_login/test", "/required_login/wild_test/1", "/required_login/1/wild_test", "/1/required_login/wild_test" })
    public void required_login_api_success_test(String requiredLoginUrl) throws Exception {
        // when
        ResultActions resultActions = mockMvc.perform(get(requiredLoginUrl));

        // then
        resultActions.andExpect(status().isNotFound());
    }

    @DisplayName("로그인 필수 API에 로그인이 없으면 에러 메세지를 응답한다.")
    @ParameterizedTest
    @ValueSource(strings = { "/required_login/test", "/required_login/wild_test/1", "/required_login/1/wild_test", "/1/required_login/wild_test" })
    public void required_login_api_fail_test(String requiredLoginUrl) throws Exception {
        // when
        ResultActions resultActions = mockMvc.perform(get(requiredLoginUrl));

        // then
        resultActions.andExpect(status().isUnauthorized());
        resultActions.andExpect(jsonPath("$.status").value("error"));
        resultActions.andExpect(jsonPath("$.message").value("로그인 필요"));
        resultActions.andExpect(jsonPath("$.data").isEmpty());
    }
}