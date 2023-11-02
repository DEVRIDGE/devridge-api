package io.devridge.api.domain.book;

import io.devridge.api.domain.BaseTimeEntity;
import io.devridge.api.domain.roadmap.CourseDetail;
import io.devridge.api.dto.admin.item.BookModifyFormDto;
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

    @JoinColumn(name = "course_detail_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private CourseDetail courseDetail;

    @Builder
    public CourseBook(Long id, String title, String url, String thumbnail, BookSource source, CourseDetail courseDetail) {
        this.id = id;
        this.title = title;
        this.url = url;
        this.thumbnail = thumbnail;
        this.source = source;
        this.courseDetail = courseDetail;
    }

    public void modifyBookInfo(BookModifyFormDto bookModifyFormDto) {
        this.title = bookModifyFormDto.getTitle();
        this.url = bookModifyFormDto.getUrl();
        this.thumbnail = bookModifyFormDto.getThumbnail();
        this.source = bookModifyFormDto.getType();
    }
}
