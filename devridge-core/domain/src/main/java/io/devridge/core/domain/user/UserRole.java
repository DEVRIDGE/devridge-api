package io.devridge.core.domain.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserRole {
    USER("ROLE_USER", "유저"), ADMIN("ROLE_ADMIN", "관리자");

    private final String key;
    private final String value;
}
