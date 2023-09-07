package io.devridge.api.service;

import io.devridge.api.domain.token.Token;
import io.devridge.api.domain.token.TokenRepository;
import io.devridge.api.domain.user.User;
import io.devridge.api.util.jwt.TokenDto;
import io.devridge.api.util.jwt.TokenProcess;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class TokenService {

    private final TokenProcess tokenProcess;
    private final TokenRepository tokenRepository;

    public String createAccessToken(User user) {
        TokenDto accessTokenDto = tokenProcess.createAccessToken(user);
        return accessTokenDto.getToken();
    }

    @Transactional
    public String createRefreshTokenAndSave(User user) {
        TokenDto refreshTokenDto = tokenProcess.createRefreshToken();
        Token token = tokenRepository.findByUser(user)
                .map(t -> t.changeToken(refreshTokenDto.getToken()))
                .orElseGet(() -> saveToken(refreshTokenDto, user));

        return token.getContent();
    }

    private Token saveToken(TokenDto refreshTokenDto, User user) {
        return tokenRepository.save(createToken(refreshTokenDto, user));
    }

    private Token createToken(TokenDto tokenDto, User user) {
        return Token.builder()
                .content(tokenDto.getToken())
                .user(user)
                .expiredAt(tokenDto.getExpiredAt())
                .build();
    }
}
