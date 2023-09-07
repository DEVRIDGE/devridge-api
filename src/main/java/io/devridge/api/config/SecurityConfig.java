package io.devridge.api.config;

import io.devridge.api.config.auth.OAuth2FailHandler;
import io.devridge.api.config.auth.OAuth2SuccessHandler;
import io.devridge.api.config.auth.OAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    private final Environment environment;
    private final OAuth2UserService oauth2UserService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final OAuth2FailHandler oAuth2FailHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        configureDevSettings(http);

        return http
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .headers(config -> config.frameOptions().disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2Login(loginConfigurer -> {
                    loginConfigurer.userInfoEndpoint().userService(oauth2UserService);
                    loginConfigurer.successHandler(oAuth2SuccessHandler);
                    loginConfigurer.failureHandler(oAuth2FailHandler);
                })
                .exceptionHandling(config -> {
//                    config.authenticationEntryPoint(authenticationEntryPointResponseHandler);
//                    config.accessDeniedHandler(this::accessDeniedResponseHandler);
                })
                .authorizeRequests(requests -> requests.anyRequest().permitAll())

                .build();
    }

    private CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.setAllowCredentials(true);
        configuration.setAllowedOriginPatterns(List.of("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

//    private void authenticationEntryPointResponseHandler(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) {
//        log.error("request.getRequestURI() = {}, ", request.getRequestURI());
//        log.error("AuthenticationException = {}", exception.getMessage());
//        CommonResponseHandler.handleException(response, "로그인이 필요합니다.", HttpStatus.UNAUTHORIZED);
//    }
//
//    private void accessDeniedResponseHandler(HttpServletRequest request, HttpServletResponse response, AccessDeniedException exception) {
//        log.error("request.getRequestURI() = {}, ", request.getRequestURI());
//        log.error("AccessDeniedException = {}", exception.getMessage());
//        CommonResponseHandler.handleException(response, "권한이 없습니다.", HttpStatus.FORBIDDEN);
//    }

    private void configureDevSettings(HttpSecurity http) throws Exception {
        if (isProfileActive("dev") || isProfileActive("test")) {
            http
                    .headers(config -> config.frameOptions().disable())
                    .csrf(AbstractHttpConfigurer::disable);
        }
    }

    private boolean isProfileActive(String profile) { return List.of(environment.getActiveProfiles()).contains(profile); }
}
