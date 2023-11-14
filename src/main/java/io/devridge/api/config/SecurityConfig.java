package io.devridge.api.config;

import io.devridge.api.config.auth.OAuth2FailHandler;
import io.devridge.api.config.auth.OAuth2SuccessHandler;
import io.devridge.api.config.auth.OAuth2UserService;
import io.devridge.api.config.filter.JwtAuthorizationFilter;
import io.devridge.api.domain.user.UserRepository;
import io.devridge.api.util.jwt.TokenProcess;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {
    private final Environment environment;
    private final OAuth2UserService oauth2UserService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final OAuth2FailHandler oAuth2FailHandler;
    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final AccessDeniedHandler accessDeniedHandler;
    private final TokenProcess tokenProcess;
    private final UserRepository userRepository;

    @Order(2)
    @Bean
    public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {
        configureDevSettings(http);

        http
            .httpBasic().disable()
            .csrf().disable()
            .formLogin().disable()
            .cors().configurationSource(corsConfigurationSource()).and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
            .oauth2Login()
                .userInfoEndpoint().userService(oauth2UserService).and()
                .successHandler(oAuth2SuccessHandler)
                .failureHandler(oAuth2FailHandler).and()
            .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler).and()
            .authorizeRequests()
                .antMatchers(AuthEndpoints.REQUIRED_STATIC_AUTH_URLS).authenticated()
                .antMatchers(AuthEndpoints.REQUIRED_WILDCARD_AUTH_URLS).authenticated()
                .anyRequest().permitAll().and()
            .addFilterBefore(new JwtAuthorizationFilter(tokenProcess, userRepository, authenticationEntryPoint), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Order(1)
    @Bean
    public SecurityFilterChain adminSecurityFilterChain(HttpSecurity http) throws Exception {
        configureDevSettings(http);

        http
            .antMatcher("/admin/**")
                .httpBasic().disable()
                .csrf().disable()
                .cors().configurationSource(corsConfigurationSource()).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED).and()
                .formLogin()
                    .loginPage("/admin/login")
                    .loginProcessingUrl("/admin/login")
                    .usernameParameter("email")
                    .passwordParameter("password")
                    .defaultSuccessUrl("/admin")
                .failureHandler(this::adminFailureHandler).and()
                .exceptionHandling()
                    .authenticationEntryPoint(this::adminAuthenticationEntryPointResponseHandler)
                    .accessDeniedHandler(this::adminAccessDeniedResponseHandler).and()
                .authorizeRequests()
                    .antMatchers("/admin/login").permitAll()
                    .antMatchers("/admin/**").hasRole("ADMIN").and()
                .logout()
                    .logoutUrl("/admin/logout")
                    .logoutSuccessUrl("/admin/login?error=logout")
                    .deleteCookies("JSESSIONID")
                    .invalidateHttpSession(true);

        return http.build();
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

    private void configureDevSettings(HttpSecurity http) throws Exception {
        if (isProfileActive("dev") || isProfileActive("test")) {
            http
                .headers().frameOptions().disable();
        }
    }

    private boolean isProfileActive(String profile) { return List.of(environment.getActiveProfiles()).contains(profile); }

    private void adminFailureHandler(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        log.error("request.getRequestURI() = {}, ", request.getRequestURI());
        log.error("AuthenticationException = {}", exception.getMessage());
        response.sendRedirect("/admin/login?error=loginFail");
    }

    private void adminAuthenticationEntryPointResponseHandler(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        log.error("request.getRequestURI() = {}, ", request.getRequestURI());
        log.error("AuthenticationException = {}", exception.getMessage());
        response.sendRedirect("/admin/login?error=needLogin");
    }

    private void adminAccessDeniedResponseHandler(HttpServletRequest request, HttpServletResponse response, AccessDeniedException exception) throws IOException {
        log.error("request.getRequestURI() = {}, ", request.getRequestURI());
        log.error("AccessDeniedException = {}", exception.getMessage());
        response.sendRedirect("/admin/login?error=accessDenied");
    }
}
