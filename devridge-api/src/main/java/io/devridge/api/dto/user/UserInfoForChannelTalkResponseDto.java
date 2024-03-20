package io.devridge.api.dto.user;

import io.devridge.core.domain.user.User;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserInfoForChannelTalkResponseDto {
    private final String email;
    private final String name;
    private final String profilePicture;
    private final String provider;

    @Builder
    public UserInfoForChannelTalkResponseDto(User user) {
        this.email = user.getEmail();
        this.name = user.getName();
        this.profilePicture = user.getProfilePicture();
        this.provider = user.getProvider();
    }
}
