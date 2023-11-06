package io.devridge.api.dto.item;

import io.devridge.api.domain.book.CourseBook;
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

    public CourseItemResponseDto(String courseName, String courseDetailName, String courseDetailDescription, List<CourseVideoWithLikeDto> courseVideoWithLikeDtoList, List<CourseBook> courseBookList) {
        this.courseTitle = courseName;
        this.courseDetailTitle = courseDetailName;
        this.courseDetailDescription = courseDetailDescription;
        this.courseVideos = courseVideoWithLikeDtoList;
        this.courseBooks = courseBookList.stream()
                .map(CourseBookDto::new)
                .collect(Collectors.toList());
    }
}
