package io.devridge.core.domain.education_materials.book;

import io.devridge.core.domain.common.BaseTimeEntity;
import io.devridge.core.domain.roadmap.CourseDetail;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Entity
public class CourseBook extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_book_id")
    private Long id;

    @Column(name = "course_book_title")
    private String title;

    @Column(name = "course_book_url")
    private String url;

    @Column(name = "course_book_thumbnail")
    private String thumbnail;

    @Enumerated(EnumType.STRING)
    @Column(name = "course_book_source")
    private BookSource source;

    @Enumerated(EnumType.STRING)
    @Column(name = "course_book_language")
    private BookLanguage language;

    @JoinColumn(name = "course_detail_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private CourseDetail courseDetail;
}
