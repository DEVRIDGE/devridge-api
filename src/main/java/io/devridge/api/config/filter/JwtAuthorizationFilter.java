package io.devridge.api.config.filter;

import io.devridge.api.config.auth.LoginUser;
import io.devridge.api.config.filter.exception.*;
import io.devridge.api.domain.user.User;
import io.devridge.api.domain.user.UserRepository;
import io.devridge.api.handler.ex.UserNotFoundException;
import io.devridge.api.util.jwt.TokenProvider;
import io.devridge.api.util.jwt.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static io.devridge.api.util.jwt.JwtSetting.JWT_HEADER;
import static io.devridge.api.util.jwt.JwtSetting.TOKEN_PREFIX;

@Slf4j
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private final TokenProvider tokenProvider;
    private final UserRepository userRepository;
    private final AuthenticationEntryPoint authenticationEntryPoint;

    public JwtAuthorizationFilter(TokenProvider tokenProvider, AuthenticationManager authenticationManager, UserRepository userRepository, AuthenticationEntryPoint authenticationEntryPoint) {
        super(authenticationManager);
        this.tokenProvider = tokenProvider;
        this.userRepository = userRepository;
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        try {
            if(isHeaderVerify(request)) {
                String token = request.getHeader(JWT_HEADER).replace(TOKEN_PREFIX, "");
                Long userId = tokenProvider.verify(token);
                LoginUser loginUser = getLoginUser(userId);
                Authentication authenticationToken = new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
            chain.doFilter(request, response);

        }  catch (JwtVerifyException exception) {
            log.error("JwtVerifyException = {}", exception.getMessage());
            authenticationEntryPoint.commence(request, response, new JwtVerifyAuthenticationException());
        } catch (JwtExpiredException exception) {
            log.error("JwtExpiredException = {}", exception.getMessage());
            authenticationEntryPoint.commence(request, response, new JwtExpiredAuthenticationException());
        } catch (JwtNotHaveIdException exception) {
            log.error("JwtNotHaveIdException = {}", exception.getMessage());
            authenticationEntryPoint.commence(request, response, new JwtNotHaveIdAuthenticationException());
        } catch (JwtIdConversionException exception) {
            log.error("JwtIdConversionException = {}", exception.getMessage());
            authenticationEntryPoint.commence(request, response, new JwtIdConversionAuthenticationException());
        } catch (UserNotFoundException exception) {
            log.error("UserNotFoundException = {}", exception.getMessage());
            authenticationEntryPoint.commence(request, response, new UserNotFoundAuthenticationException());
        }
    }

    private LoginUser getLoginUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User with ID " + userId + " not found."));
        return LoginUser.builder().user(user).build();
    }

    private boolean isHeaderVerify(HttpServletRequest request) {
        String header = request.getHeader(JWT_HEADER);
        return header != null && !header.isEmpty();
    }
}
