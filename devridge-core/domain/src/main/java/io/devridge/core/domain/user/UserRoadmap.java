package io.devridge.core.domain.user;

import io.devridge.core.domain.common.BaseTimeEntity;
import io.devridge.core.domain.roadmap.Roadmap;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Getter
@Builder
@AllArgsConstructor
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

    public void changeStudyStatus(StudyStatus studyStatus) {
        this.studyStatus = studyStatus;
    }
}
