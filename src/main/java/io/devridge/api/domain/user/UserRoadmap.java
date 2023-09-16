package io.devridge.api.domain.user;

import io.devridge.api.domain.BaseTimeEntity;
import io.devridge.api.domain.roadmap.Roadmap;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Entity
public class UserRoadmap extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_roadmap_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "roadmap_study_status")
    private StudyStatus studyStatus;

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @JoinColumn(name = "roadmap_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Roadmap roadmap;

    @Builder
    public UserRoadmap(Long id, StudyStatus studyStatus, User user, Roadmap roadmap) {
        this.id = id;
        this.studyStatus = studyStatus;
        this.user = user;
        this.roadmap = roadmap;
    }
}
