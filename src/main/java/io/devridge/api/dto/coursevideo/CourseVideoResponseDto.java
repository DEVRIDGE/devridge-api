package io.devridge.api.dto.coursevideo;

import io.devridge.api.domain.video.CourseVideo;
import io.devridge.api.domain.video.VideoSource;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class CourseVideoResponseDto {
    public String courseTitle;
    private String courseDetailTitle;
    private List<CourseVideoWithLikeDto> courseVideos;

    public CourseVideoResponseDto(String courseName, String courseDetailName, List<CourseVideoWithLikeDto> courseVideoWithLikeDtoList) {
        this.courseTitle = courseName;
        this.courseDetailTitle = courseDetailName;
        this.courseVideos = courseVideoWithLikeDtoList;
    }

}
