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
    public String getOrUpdateRefreshToken(User user) {
        return tokenRepository.findByUser(user)
                .map(currentRefreshToken -> {
                    if (tokenProcess.isTokenValid(currentRefreshToken.getContent())) {
                        return currentRefreshToken.getContent();
                    }
                    return generateAndSaveNewRefreshToken(user, currentRefreshToken);
                })
                .orElseGet(() -> generateAndSaveNewRefreshToken(user, null));
    }

    private String generateAndSaveNewRefreshToken(User user, Token currentToken) {
        TokenDto refreshTokenDto = tokenProcess.createRefreshToken();

        if(currentToken != null) {
            currentToken.changeToken(refreshTokenDto.getToken());
        } else {
            tokenRepository.save(createToken(refreshTokenDto, user));
        }

        return refreshTokenDto.getToken();
    }

    private Token createToken(TokenDto tokenDto, User user) {
        return Token.builder()
                .content(tokenDto.getToken())
                .user(user)
                .expiredAt(tokenDto.getExpiredAt())
                .build();
    }
}
