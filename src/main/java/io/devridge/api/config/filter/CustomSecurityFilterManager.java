package io.devridge.api.config.filter;

import io.devridge.api.domain.user.UserRepository;
import io.devridge.api.util.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CustomSecurityFilterManager extends AbstractHttpConfigurer<CustomSecurityFilterManager, HttpSecurity> {

    private final TokenProvider tokenProvider;
    private final UserRepository userRepository;
    private final AuthenticationEntryPoint authenticationEntryPoint;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        AuthenticationManager authenticationManager = getBuilder().getSharedObject(AuthenticationManager.class);
        getBuilder().addFilter(new JwtAuthorizationFilter(tokenProvider, authenticationManager, userRepository, authenticationEntryPoint));
        super.configure(http);
    }
}
