package io.devridge.admin.config;

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
import org.springframework.security.web.SecurityFilterChain;
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
public class AdminSecurityConfig {
    private final Environment environment;

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
