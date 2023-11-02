package io.devridge.api.dto.admin.item;

import io.devridge.api.domain.book.BookSource;
import lombok.Builder;
import lombok.Getter;

@Getter
public class BookModifyFormDto {
    private Long id;
    private String title;
    private String url;
    private String thumbnail;
    private BookSource type;

    @Builder
    public BookModifyFormDto(Long id, String title, String url, String thumbnail, BookSource type) {
        this.id = id;
        this.title = title;
        this.url = url;
        this.thumbnail = thumbnail;
        this.type = type;
    }
}
