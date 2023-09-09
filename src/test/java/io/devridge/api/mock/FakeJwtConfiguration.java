package io.devridge.api.mock;

import io.devridge.api.util.jwt.TokenProvider;
import org.springframework.context.annotation.Bean;

public class FakeJwtConfiguration {
    @Bean
    public TokenProvider tokenProvider() {
        return new FakeTokenProvider();
    }
}
