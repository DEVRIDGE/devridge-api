package io.devridge.api.dto.user;

import io.devridge.core.domain.user.User;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserInfoForChannelTalkResponseDto {
    private String email;
    private String name;
    private String profilePicture;
    private String provider;
    @Builder
    public UserInfoForChannelTalkResponseDto(User user) {
        this.email = user.getEmail();
        this.name = user.getName();
        this.profilePicture = user.getProfilePicture();
        this.provider = user.getProvider();
    }
}
