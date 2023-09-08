package io.devridge.api.util.jwt;

import io.devridge.api.domain.user.User;
import io.devridge.api.util.time.TimeProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;

import static io.devridge.api.util.jwt.JwtSetting.ACCESS_TOKEN_VALID_TIME;
import static io.devridge.api.util.jwt.JwtSetting.REFRESH_TOKEN_VALID_TIME;

@RequiredArgsConstructor
@Component
public class TokenProcess {

    private final TimeProvider timeProvider;
    private final TokenProvider tokenProvider;

    public TokenDto createAccessToken(User user) {
        LocalDateTime tokenExpiredAt = timeProvider.getCurrentTime().plus(Duration.ofMillis(ACCESS_TOKEN_VALID_TIME));
        Date tokenExpiredDate = timeProvider.convertToJavaUtilDate(tokenExpiredAt);
        String accessToken = tokenProvider.createAccessToken(user, tokenExpiredDate);

        return TokenDto.builder()
                .token(accessToken)
                .expiredAt(tokenExpiredAt)
                .build();
    }

    public TokenDto createRefreshToken() {
        LocalDateTime currentTime = timeProvider.getCurrentTime();
        LocalDateTime tokenExpiredAt = currentTime.plus(Duration.ofMillis(REFRESH_TOKEN_VALID_TIME));
        String refreshToken = tokenProvider.createRefreshToken(timeProvider.convertToJavaUtilDate(currentTime), timeProvider.convertToJavaUtilDate(tokenExpiredAt));

        return TokenDto.builder()
                .token(refreshToken)
                .expiredAt(tokenExpiredAt)
                .build();
    }

//
//    public Authentication getAuthentication(String token) {
//        DecodedJWT decodedJWT = JWT.decode(token);
//        Collection<GrantedAuthority> authorities = new ArrayList<>();
//        authorities.add(() -> decodedJWT.getClaim("role").toString());
//
//        return new UsernamePasswordAuthenticationToken(decodedJWT.getClaim("email").toString(), "", authorities);
//    }
//
//    public String getUserEmail(String token) {
//        try {
//            DecodedJWT decodedJWT = JWT.decode(token);
//            return decodedJWT.getClaim("email").asString();
//        } catch (JWTDecodeException e) {
//            log.error("Error decoding JWT token: {}", e.getMessage());
//        } catch (Exception e) {
//            log.error("Error parsing JWT token: {}", e.getMessage());
//        }
//        return null;
//
//
//    }
//
//
//
//    public String resolveToken(HttpServletRequest req) {
//        String authorizationHeader = req.getHeader("Authorization");
//        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
//            return authorizationHeader.replace("Bearer ", "");
//        }
//        return null;
//    }
//
//    public boolean validateTokenExceptExpiration(String token) {
//        try {
//            Algorithm algorithm = Algorithm.HMAC512(secretKey);
//            JWTVerifier verifier = JWT.require(algorithm).build();
//            DecodedJWT decodedJWT = verifier.verify(token);
//
//            Date expirationDate = decodedJWT.getExpiresAt();
//            return expirationDate.after(new Date());
//        } catch (Exception e) {
//            return false;
//        }
//    }


}
