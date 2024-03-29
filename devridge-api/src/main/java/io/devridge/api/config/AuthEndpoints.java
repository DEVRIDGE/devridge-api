package io.devridge.api.config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthEndpoints {
    public static final String[] REQUIRED_STATIC_AUTH_URLS = { "/required_login/test", "/users/channeltalkinfo", "/videos/like"};
    public static final String[] REQUIRED_WILDCARD_AUTH_URLS = { "/required_login/wild_test/**", "/required_login/**/wild_test", "/**/required_login/wild_test", "/courses/**/change-studystatus"};
    public static final String[] OPTIONAL_STATIC_AUTH_URLS = { "/courses", "/optional_login/test" };
    public static final String[] OPTIONAL_WILDCARD_AUTH_URLS = { "/courses/**", "/courses/**/coursedetail/**/items", "/optional_login/wild_test/**", "/optional_login/**/wild_test", "/**/optional_login/wild_test" };
}