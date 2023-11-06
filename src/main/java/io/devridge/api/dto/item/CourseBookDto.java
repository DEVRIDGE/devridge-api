package io.devridge.api.dto.item;

import io.devridge.api.domain.book.BookSource;
import io.devridge.api.domain.book.CourseBook;
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
