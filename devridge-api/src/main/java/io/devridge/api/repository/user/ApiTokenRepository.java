package io.devridge.api.repository.user;

import io.devridge.core.domain.user.Token;
import io.devridge.core.domain.user.TokenRepository;
import io.devridge.core.domain.user.User;

import java.util.Optional;

public interface ApiTokenRepository extends TokenRepository {
    Optional<Token> findByUser(User user);

    Optional<Token> findByContent(String content);
}
