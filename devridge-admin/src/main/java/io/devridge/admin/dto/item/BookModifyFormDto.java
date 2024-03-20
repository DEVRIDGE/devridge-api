package io.devridge.admin.dto.item;

import io.devridge.core.domain.education_materials.book.BookSource;
import lombok.Builder;
import lombok.Getter;

@Getter
public class BookModifyFormDto {
    private Long id;
    private String title;
    private String url;
    private String thumbnail;
    private BookSource source;

    @Builder
    public BookModifyFormDto(Long id, String title, String url, String thumbnail, BookSource source) {
        this.id = id;
        this.title = title;
        this.url = url;
        this.thumbnail = thumbnail;
        this.source = source;
    }
}
