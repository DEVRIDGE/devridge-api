package io.devridge.api.config.auth;

import lombok.Getter;

import java.util.Map;
import java.util.Optional;

@Getter
public class KakaoProfile {
    private Long id;
    private KakaoAccount kakaoAccount;

    public KakaoProfile(Map<String, Object> attributes) {
        this.id = (Long) attributes.get("id");
        Map<String, Object> kakaoAtrributes = (Map<String, Object>) attributes.get("kakao_account");
        this.kakaoAccount = kakaoAtrributes != null ? new KakaoAccount(kakaoAtrributes) : null;
    }

    @Getter
    class KakaoAccount {
        private Profile profile;
        private String email;

        public KakaoAccount(Map<String, Object> kakaoAccount) {
            /**
             * 카카오에서 email을 선택사항으로 지정 (승인 필요)
             */
            this.email = Optional.ofNullable((String) kakaoAccount.get("email")).orElse(String.valueOf(id));
            Map<String, Object> profileAttributes = (Map<String, Object>) kakaoAccount.get("profile");
            this.profile = profileAttributes != null ? new Profile(profileAttributes) : null;
        }

        @Getter
        class Profile {
            private String nickname;
            private String thumbnailImageUrl;

            public Profile(Map<String, Object> profile) {
                this.nickname = (String) profile.get("nickname");
                this.thumbnailImageUrl = (String) profile.get("thumbnail_image_url");
            }
        }
    }
}