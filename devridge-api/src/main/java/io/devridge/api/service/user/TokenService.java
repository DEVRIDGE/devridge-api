package io.devridge.api.service.user;


import io.devridge.api.dto.user.ReissueTokenRequestDto;
import io.devridge.api.dto.user.ReissueTokenResponseDto;
import io.devridge.api.handler.ex.NotHaveRefreshTokenException;
import io.devridge.api.repository.user.ApiTokenRepository;
import io.devridge.api.util.jwt.TokenDto;
import io.devridge.api.util.jwt.TokenProcess;
import io.devridge.core.domain.user.Token;
import io.devridge.core.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class TokenService {
    private final TokenProcess tokenProcess;
    private final ApiTokenRepository tokenRepository;

    public String createAccessToken(User user) {
        TokenDto accessTokenDto = tokenProcess.createAccessToken(user);
        return accessTokenDto.getTokenContent();
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

    @Transactional(readOnly = true)
    public ReissueTokenResponseDto reissue(ReissueTokenRequestDto reissueTokenRequestDto) {
        Token refreshToken = tokenRepository.findByContent(reissueTokenRequestDto.getToken())
                .orElseThrow(NotHaveRefreshTokenException::new);

        tokenProcess.tokenValidOrThrowException(refreshToken.getContent());
        TokenDto accessTokenDto = tokenProcess.createAccessToken(refreshToken.getUser());

        return new ReissueTokenResponseDto(accessTokenDto);
    }

    private String generateAndSaveNewRefreshToken(User user, Token currentToken) {
        TokenDto refreshTokenDto = tokenProcess.createRefreshToken();

        if(currentToken != null) {
            currentToken.changeContent(refreshTokenDto.getTokenContent());
        } else {
            tokenRepository.save(createToken(refreshTokenDto, user));
        }

        return refreshTokenDto.getTokenContent();
    }

    private Token createToken(TokenDto tokenDto, User user) {
        return Token.builder()
                .content(tokenDto.getTokenContent())
                .user(user)
                .expiredAt(tokenDto.getExpiredAt())
                .build();
    }
}
