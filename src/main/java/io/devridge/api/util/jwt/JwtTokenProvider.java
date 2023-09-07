package io.devridge.api.util.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import io.devridge.api.domain.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtTokenProvider implements TokenProvider {

    @Value("${spring.jwt.secretKey}")
    private String secretKey;

    @Override
    public String createAccessToken(User user, Date tokenExpiredDate) {
        return JWT.create()
                .withSubject(user.getEmail())
                .withClaim("id", user.getId())
                .withClaim("role", user.getRoleKey())
                .withExpiresAt(tokenExpiredDate)
                .sign(Algorithm.HMAC512(secretKey));
    }

    @Override
    public String createRefreshToken(Date now, Date tokenExpiredDate) {
        return JWT.create()
                .withIssuedAt(now)
                .withExpiresAt(tokenExpiredDate)
                .sign(Algorithm.HMAC512(secretKey));
    }

    @Override
    public Long verify(String token) {
        return null;
//        DecodedJWT decodedJWT = decodingToken(token);
//        Claim JwtIdClaim = getIdByJwt(decodedJWT);
//
//        return convertIdClaimToLong(JwtIdClaim);
    }
//
//    private DecodedJWT decodingToken(String token) {
//        try {
//            return JWT.require(Algorithm.HMAC512(JwtVO.SECRET)).build().verify(token);
//        } catch (TokenExpiredException exception) {
//            log.error("TokenExpiredException = {}", exception.getMessage());
//            throw new JwtExpiredException();
//        } catch (JWTVerificationException exception) {
//            log.error("JWTVerificationException = {}", exception.getMessage());
//            throw new JwtVerifyException();
//        }
//    }
//
//    private long convertIdClaimToLong(Claim idClaim) throws JwtIdConversionException {
//        try {
//            return idClaim.asLong();
//        } catch (NumberFormatException exception) {
//            log.error("idClaim = {}", idClaim);
//            log.error("NumberFormatException  = {}", exception.getMessage());
//            throw new JwtIdConversionException();
//        }
//    }
//
//    private Claim getIdByJwt(DecodedJWT decodedJWT) throws JwtNotHaveIdException {
//        Claim idClaim = decodedJWT.getClaim("id");
//        if (idClaim.isNull()) {
//            throw new JwtNotHaveIdException();
//        }
//        return idClaim;
//    }
}
