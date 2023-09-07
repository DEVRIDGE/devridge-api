package io.devridge.api.domain.token;

import io.devridge.api.domain.BaseTimeEntity;
import io.devridge.api.domain.user.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Entity
public class Token extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "token_id")
    private Long id;

    @Column(name = "token_content")
    private String content;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false, name = "token_expired_at")
    private LocalDateTime expiredAt;

    @Builder
    public Token(String content, User user, LocalDateTime expiredAt) {
        this.content = content;
        this.user = user;
        this.expiredAt = expiredAt;
    }

    public Token changeToken(String token) {
        this.content = token;
        return this;
    }
}
