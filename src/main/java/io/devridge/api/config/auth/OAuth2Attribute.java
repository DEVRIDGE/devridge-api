package io.devridge.api.config.auth;

import io.devridge.api.handler.ex.UnsupportedProviderException;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;

import java.util.Map;
import java.util.Optional;

@ToString
@Getter
@Builder(access = AccessLevel.PRIVATE)
public class OAuth2Attribute {
    private String email;
    private String name;
    private String picture;
    private String provider;
    private String providerIdKey;
    private Map<String, Object> attributes;

    public static OAuth2Attribute of(OAuth2UserRequest userRequest, Map<String, Object> attributes) {
        String provider = userRequest.getClientRegistration().getRegistrationId();
        String providerIdKey = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
        return of(provider, providerIdKey, attributes);
    }

    private static OAuth2Attribute of(String provider, String providerIdKey, Map<String, Object> attributes) {
        switch (provider) {
            case "google":
                return ofGoogle(provider, providerIdKey, attributes);
            default:
                throw new UnsupportedProviderException(provider);
        }
    }

    private static OAuth2Attribute ofGoogle(String provider, String providerIdKey, Map<String, Object> attributes) {
        return OAuth2Attribute.builder()
                .email(getStringAttribute(attributes, "email"))
                .name(getStringAttribute(attributes, "name"))
                .picture(getStringAttribute(attributes, "picture"))
                .provider(provider)
                .providerIdKey(providerIdKey)
                .attributes(attributes)
                .build();
    }

    private static String getStringAttribute(Map<String, Object> attributes, String key) {
        return Optional.ofNullable(attributes.get(key))
                .map(Object::toString)
                .orElse(null);
    }
}