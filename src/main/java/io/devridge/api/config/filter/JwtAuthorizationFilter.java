package io.devridge.api.config.filter;

import io.devridge.api.config.AuthEndpoints;
import io.devridge.api.config.auth.LoginUser;
import io.devridge.api.config.filter.exception.*;
import io.devridge.api.domain.user.User;
import io.devridge.api.domain.user.UserRepository;
import io.devridge.api.handler.ex.UserNotFoundException;
import io.devridge.api.util.jwt.TokenProvider;
import io.devridge.api.util.jwt.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

import static io.devridge.api.util.jwt.JwtSetting.JWT_HEADER;
import static io.devridge.api.util.jwt.JwtSetting.TOKEN_PREFIX;

@Slf4j
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;
    private final UserRepository userRepository;
    private final AuthenticationEntryPoint authenticationEntryPoint;

    public JwtAuthorizationFilter(TokenProvider tokenProvider, UserRepository userRepository, AuthenticationEntryPoint authenticationEntryPoint) {
        this.tokenProvider = tokenProvider;
        this.userRepository = userRepository;
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        try {
            if (matchesAuthEndpoints(request.getRequestURI())) {
                if (isHeaderVerify(request)) {
                    String token = request.getHeader(JWT_HEADER).replace(TOKEN_PREFIX, "");
                    Long userId = tokenProvider.verify(token);
                    LoginUser loginUser = getLoginUser(userId);
                    Authentication authenticationToken = new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
            chain.doFilter(request, response);
        } catch (JwtVerifyException | JwtExpiredException | JwtNotHaveIdException | JwtIdConversionException exception) {
            log.error("Authentication error: {}", exception.getMessage());
            authenticationEntryPoint.commence(request, response, new JwtVerifyAuthenticationException());
        } catch (UserNotFoundException exception) {
            log.error("User not found: {}", exception.getMessage());
            authenticationEntryPoint.commence(request, response, new UserNotFoundAuthenticationException());
        }
    }

    private boolean matchesAuthEndpoints(String requestURI) {
        return Arrays.stream(AuthEndpoints.REQUIRED_STATIC_AUTH_URLS).anyMatch(requestURI::equals)
                || Arrays.stream(AuthEndpoints.REQUIRED_WILDCARD_AUTH_URLS).anyMatch(pattern -> matchWildcardPattern(requestURI, pattern))
                || Arrays.stream(AuthEndpoints.OPTIONAL_STATIC_AUTH_URLS).anyMatch(requestURI::equals)
                || Arrays.stream(AuthEndpoints.OPTIONAL_WILDCARD_AUTH_URLS).anyMatch(pattern -> matchWildcardPattern(requestURI, pattern));
    }

    private LoginUser getLoginUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User with ID " + userId + " not found."));
        return LoginUser.builder().user(user).build();
    }

    private boolean isHeaderVerify(HttpServletRequest request) {
        String header = request.getHeader(JWT_HEADER);
        return header != null && !header.isEmpty();
    }

    private boolean matchWildcardPattern(String requestURI, String pattern) {
        String regexPattern = pattern
                .replace("/", "\\/")
                .replace("**", "[a-zA-Z0-9]+");

        String regex = "^" + regexPattern + "$";
        return requestURI.matches(regex);
    }
}
