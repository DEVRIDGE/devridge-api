package io.devridge.api.dto.education_materials;

import io.devridge.core.domain.education_materials.book.BookSource;
import io.devridge.core.domain.education_materials.book.CourseBook;
import lombok.Getter;

@Getter
public class CourseBookDto {
    private final Long id;
    private final String title;
    private final String url;
    private final String thumbnail;
    private final BookSource source;

    public CourseBookDto(CourseBook courseBook) {
        this.id = courseBook.getId();
        this.title = courseBook.getTitle();
        this.url = courseBook.getUrl();
        this.thumbnail = courseBook.getThumbnail();
        this.source = courseBook.getSource();
    }
}
