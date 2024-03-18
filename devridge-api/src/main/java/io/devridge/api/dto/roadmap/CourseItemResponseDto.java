package io.devridge.api.dto.roadmap;

import io.devridge.api.dto.education_materials.CourseBookDto;
import io.devridge.api.dto.education_materials.CourseVideoWithLikeDto;
import io.devridge.core.domain.education_materials.book.CourseBook;
import io.devridge.core.domain.roadmap.CourseToDetail;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class CourseItemResponseDto {
    private final String courseTitle;
    private final String courseDetailTitle;
    private final String courseDetailDescription;
    private final List<CourseVideoWithLikeDto> courseVideos;
    private final List<CourseBookDto> courseBooks;

    public CourseItemResponseDto(CourseToDetail courseToDetail, List<CourseVideoWithLikeDto> courseVideoWithLikeDtoList, List<CourseBook> courseBookList) {
        this.courseTitle = courseToDetail.getCourse().getName();
        this.courseDetailTitle = courseToDetail.getCourseDetail().getName();
        this.courseDetailDescription = courseToDetail.getCourseDetail().getDescription();
        this.courseVideos = courseVideoWithLikeDtoList;
        this.courseBooks = courseBookList.stream()
                .map(CourseBookDto::new)
                .collect(Collectors.toList());
    }
}
