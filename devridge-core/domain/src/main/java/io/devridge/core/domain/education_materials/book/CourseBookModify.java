package io.devridge.core.domain.education_materials.book;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CourseBookModify {
    private final String title;
    private final String url;
    private final String thumbnail;
    private final BookSource source;

    @Builder
    public CourseBookModify(String title, String url, String thumbnail, BookSource source) {
        this.title = title;
        this.url = url;
        this.thumbnail = thumbnail;
        this.source = source;
    }
}
