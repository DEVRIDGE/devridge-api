package io.devridge.admin.util.converter;

import io.devridge.admin.dto.item.BookModifyFormDto;
import io.devridge.admin.dto.item.VideoModifyFormDto;
import io.devridge.core.domain.education_materials.book.CourseBookModify;
import io.devridge.core.domain.education_materials.video.CourseVideoModify;

public class CourseItemConverter {
    public static CourseVideoModify convertVideoModifyFormDtoToCourseVideoModify(VideoModifyFormDto videoModifyFormDto) {
        return CourseVideoModify.builder()
                .title(videoModifyFormDto.getTitle())
                .url(videoModifyFormDto.getUrl())
                .owner(videoModifyFormDto.getOwner())
                .thumbnail(videoModifyFormDto.getThumbnail())
                .source(videoModifyFormDto.getSource())
                .build();
    }

    public static CourseBookModify convertBookModifyFormDtoToCourseBookModify(BookModifyFormDto bookModifyFormDto) {
        return CourseBookModify.builder()
                .title(bookModifyFormDto.getTitle())
                .url(bookModifyFormDto.getUrl())
                .thumbnail(bookModifyFormDto.getThumbnail())
                .source(bookModifyFormDto.getSource())
                .build();
    }

}
