package io.devridge.api.config.auth;

import io.devridge.api.handler.ex.UnsupportedProviderException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OAuth2AttributeTest {

    @DisplayName("구글 SNS 로그인 정보에서 필요한 정보를 생성한다.")
    @Test
    public void make_attribute_google_test() {
        // given
        String provider = "google";
        Map<String, Object> attributes = Map.of(
                "email", "test@test.com",
                "name", "test",
                "picture", "test.jpg"
        );

        // when
        OAuth2Attribute oAuth2Attribute = OAuth2Attribute.of(provider, attributes);

        // then
        assertThat(oAuth2Attribute.getEmail()).isEqualTo("test@test.com");
        assertThat(oAuth2Attribute.getName()).isEqualTo("test");
        assertThat(oAuth2Attribute.getPicture()).isEqualTo("test.jpg");
        assertThat(oAuth2Attribute.getProvider()).isEqualTo("google");
        assertThat(oAuth2Attribute.getAttributes()).isEqualTo(attributes);
    }

    @DisplayName("지원하지 않는 SNS으로 로그인 시도시 예외를 발생시킨다.")
    @Test
    public void unsupported_provider_exception_test() {
        // given
        String provider = "wrongProvider";
        Map<String, Object> attributes = Map.of(
                "email", "test@test.com",
                "name", "test",
                "picture", "test.jpg"
        );

        // then
        assertThatThrownBy(() -> OAuth2Attribute.of(provider, attributes))
                .isInstanceOf(UnsupportedProviderException.class);
    }
}