package io.devridge.api.util.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.devridge.api.domain.user.User;
import io.devridge.api.util.jwt.exception.JwtExpiredException;
import io.devridge.api.util.jwt.exception.JwtIdConversionException;
import io.devridge.api.util.jwt.exception.JwtNotHaveIdException;
import io.devridge.api.util.jwt.exception.JwtVerifyException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

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
        DecodedJWT decodedJWT = decodingToken(token);
        Claim JwtIdClaim = getIdByJwt(decodedJWT);

        return convertIdClaimToLong(JwtIdClaim);
    }

    private DecodedJWT decodingToken(String token) {
        try {
            return JWT.require(Algorithm.HMAC512(secretKey)).build().verify(token);
        } catch (TokenExpiredException exception) {
            throw new JwtExpiredException(exception.getMessage());
        } catch (JWTVerificationException exception) {
            throw new JwtVerifyException(exception.getMessage());
        }
    }

    private long convertIdClaimToLong(Claim idClaim) throws JwtIdConversionException {
        try {
            return idClaim.asLong();
        } catch (NumberFormatException exception) {
            String message = "idClaim : " + idClaim + ", NumberFormatException : " + exception.getMessage();
            throw new JwtIdConversionException(message);
        }
    }

    private Claim getIdByJwt(DecodedJWT decodedJWT) throws JwtNotHaveIdException {
        Claim idClaim = decodedJWT.getClaim("id");
        if (idClaim.isNull()) {
            throw new JwtNotHaveIdException();
        }
        return idClaim;
    }
}
