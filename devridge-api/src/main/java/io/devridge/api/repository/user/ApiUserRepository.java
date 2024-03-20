package io.devridge.api.repository.user;

import io.devridge.core.domain.user.User;
import io.devridge.core.domain.user.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApiUserRepository extends UserRepository {
    Optional<User> findByEmail(String email);
}
