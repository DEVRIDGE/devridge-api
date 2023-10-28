package io.devridge.api.dto;

import io.devridge.api.domain.video.CourseVideo;
import io.devridge.api.domain.video.VideoSource;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class CourseVideoResponseDto {
    public String courseTitle;
    private String courseDetailTitle;
    private List<CourseVideoDto> courseVideos;
    private Integer LikeCnt;

    public CourseVideoResponseDto(String courseName, String courseDetailName, List<CourseVideo> courseVideoList) {
        this.courseTitle = courseName;
        this.courseDetailTitle = courseDetailName;
        this.courseVideos = courseVideoList.stream().map(courseVideo -> new CourseVideoDto(courseVideo)).collect(Collectors.toList());
    }

    @Getter
    public class CourseVideoDto {
        private Long id;
        private String title;
        private Integer likeCnt;
        private String thumbnail;
        private String url;
        private VideoSource source;

        public CourseVideoDto(CourseVideo courseVideo) {
            this.id = courseVideo.getId();
            this.title = courseVideo.getTitle();
            this.likeCnt = courseVideo.getLikeCnt();
            this.thumbnail = courseVideo.getThumbnail();
            this.url = courseVideo.getUrl();
            this.source = courseVideo.getSource();
        }
    }

}
