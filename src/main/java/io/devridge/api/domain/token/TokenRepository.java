package io.devridge.api.domain.token;

import io.devridge.api.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByUser(User user);

    Optional<Token> findByContent(String content);
}
