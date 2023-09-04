package io.devridge.api.domain.user;

import io.devridge.api.domain.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "users")
@Entity
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "user_email")
    private String email;

    @Column(name = "user_name")
    private String name;

    @Column(name = "user_picture")
    private String profilePicture;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_role")
    private UserRole role;

    @Column(name = "user_provider")
    private String provider;

    @Column(name = "user_provider_id")
    private String providerId;

    @Builder
    public User(String email, String profilePicture, UserRole role, String provider, String providerId) {
        this.email = email;
        this.profilePicture = profilePicture;
        this.role = role;
        this.provider = provider;
        this.providerId = providerId;
    }

    public String getRoleKey() {
        return this.role.getKey();
    }
}
