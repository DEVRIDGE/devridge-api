package io.devridge.api.domain.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserRole {

    USER("ROLE_USER", "유저"), ADMIN("ROLE_ADMIN", "관리자");

    private final String key;
    private final String value;
}
