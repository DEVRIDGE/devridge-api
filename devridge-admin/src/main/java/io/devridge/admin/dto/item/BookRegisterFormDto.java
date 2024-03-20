package io.devridge.admin.dto.item;

import io.devridge.core.domain.education_materials.book.BookLanguage;
import io.devridge.core.domain.education_materials.book.BookSource;
import io.devridge.core.domain.education_materials.book.CourseBook;
import io.devridge.core.domain.roadmap.CourseDetail;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;

@Getter
public class BookRegisterFormDto {
    private String title;
    private String url;
    private String thumbnail;
    private BookSource source;

    @Builder
    public BookRegisterFormDto(String title, String url, String thumbnail, BookSource source) {
        this.title = title;
        this.url = url;
        this.thumbnail = thumbnail;
        this.source = source;
    }

    public CourseBook toDomain(CourseDetail courseDetail) {
        return CourseBook.builder()
                .title(title)
                .url(url)
                .thumbnail(thumbnail)
                .source(source)
                .courseDetail(courseDetail)
                .language(BookLanguage.KOR)
                .build();
    }
}
