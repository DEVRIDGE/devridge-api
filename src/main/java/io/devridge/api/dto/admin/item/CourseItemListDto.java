package io.devridge.api.dto.admin.item;

import io.devridge.api.domain.book.BookSource;
import io.devridge.api.domain.book.CourseBook;
import io.devridge.api.domain.roadmap.CourseDetail;
import io.devridge.api.domain.video.CourseVideo;
import io.devridge.api.domain.video.VideoSource;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class CourseItemListDto {
    private String detailName;
    private List<CourseVideoDto> courseVideoDtoList;
    private List<CourseBookDto> courseBookDtoList;
    private List<VideoSource> videoSourceList = Arrays.asList(VideoSource.values());
    private List<BookSource> bookSourceList = Arrays.asList(BookSource.values());

    public CourseItemListDto(CourseDetail courseDetail, List<CourseVideo> courseVideoList, List<CourseBook> courseBookList) {
        this.detailName = courseDetail.getName();
        this.courseVideoDtoList = courseVideoList.stream().map(CourseVideoDto::new).collect(Collectors.toList());
        this.courseBookDtoList = courseBookList.stream().map(CourseBookDto::new).collect(Collectors.toList());
    }

    @Getter
    public static class CourseVideoDto {
        private Long id;
        private String title;
        private String url;
        private String owner;
        private String thumbnail;
        private VideoSource source;

        public CourseVideoDto(CourseVideo courseVideo) {
            this.id = courseVideo.getId();
            this.title = courseVideo.getTitle();
            this.url = courseVideo.getUrl();
            this.owner = courseVideo.getOwner();
            this.thumbnail = courseVideo.getThumbnail();
            this.source = courseVideo.getSource();
        }
    }

    @Getter
    public static class CourseBookDto {
        private Long id;
        private String title;
        private String url;
        private String thumbnail;
        private BookSource source;

        public CourseBookDto(CourseBook courseBook) {
            this.id = courseBook.getId();
            this.title = courseBook.getTitle();
            this.url = courseBook.getUrl();
            this.thumbnail = courseBook.getThumbnail();
            this.source = courseBook.getSource();
        }
    }
}
