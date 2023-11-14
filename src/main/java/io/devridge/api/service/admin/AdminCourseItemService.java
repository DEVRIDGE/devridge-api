package io.devridge.api.service.admin;

import io.devridge.api.domain.book.CourseBook;
import io.devridge.api.domain.book.CourseBookRepository;
import io.devridge.api.domain.roadmap.CourseDetail;
import io.devridge.api.domain.roadmap.CourseDetailRepository;
import io.devridge.api.domain.video.CourseVideo;
import io.devridge.api.domain.video.CourseVideoRepository;
import io.devridge.api.domain.video.VideoSource;
import io.devridge.api.dto.admin.item.*;
import io.devridge.api.handler.ex.CourseDetailNotFoundException;
import io.devridge.api.handler.ex.NotFoundCourseBookException;
import io.devridge.api.handler.ex.NotFoundCourseVideoException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Transactional
@Service
public class AdminCourseItemService {
    private final CourseVideoRepository courseVideoRepository;
    private final CourseBookRepository courseBookRepository;
    private final CourseDetailRepository courseDetailRepository;

    @Transactional(readOnly = true)
    public CourseItemListDto findAllItemByCourseDetailId(Long courseDetailId) {
        CourseDetail courseDetail = courseDetailRepository.findById(courseDetailId)
                .orElseThrow(() -> new CourseDetailNotFoundException("코스 상세를 찾을 수 없습니다."));

        List<CourseVideo> courseVideoList = courseVideoRepository.findByCourseDetailId(courseDetailId);
        List<CourseBook> courseBookList = courseBookRepository.findByCourseDetailId(courseDetailId);

        return new CourseItemListDto(courseDetail, courseVideoList, courseBookList);
    }

    public void saveVideo(Long courseDetailId, VideoRegisterFormDto videoRegisterFormDto) {
        CourseDetail courseDetail = courseDetailRepository.findById(courseDetailId).orElseThrow(() -> new CourseDetailNotFoundException("코스 상세를 찾을 수 없습니다."));

        if (Objects.equals(videoRegisterFormDto.getThumbnail(), "") && videoRegisterFormDto.getType() == VideoSource.YOUTUBE) {
            videoRegisterFormDto.setThumbnail(youtubeThumbnailMaker(videoRegisterFormDto.getUrl()));
        }

        CourseVideo courseVideo = CourseVideo.builder()
                .title(videoRegisterFormDto.getTitle())
                .url(videoRegisterFormDto.getUrl())
                .owner(videoRegisterFormDto.getOwner())
                .thumbnail(videoRegisterFormDto.getThumbnail())
                .source(videoRegisterFormDto.getType())
                .courseDetail(courseDetail)
                .build();
        
        courseVideoRepository.save(courseVideo);
    }

    public void modifyVideo(Long courseDetailId, VideoModifyFormDto videoModifyFormDto) {
        checkCourseDetailId(courseDetailId);

        CourseVideo courseVideo = getCourseVideo(videoModifyFormDto.getId());

        String thumbnail = getDefaultYoutubeThumbnailIfAbsent(videoModifyFormDto.getThumbnail(), videoModifyFormDto.getType(), videoModifyFormDto.getUrl());
        videoModifyFormDto.setThumbnail(thumbnail);

        courseVideo.modifyVideoInfo(videoModifyFormDto);
    }

    public void deleteVideo(Long videoId) {
        CourseVideo courseVideo = getCourseVideo(videoId);
        courseVideoRepository.delete(courseVideo);
    }

    public void saveBook(Long courseDetailId, BookRegisterFormDto bookRegisterFormDto) {
        CourseDetail courseDetail = courseDetailRepository.findById(courseDetailId).orElseThrow(() -> new CourseDetailNotFoundException("코스 상세를 찾을 수 없습니다."));
        CourseBook courseBook = CourseBook.builder()
                .title(bookRegisterFormDto.getTitle())
                .url(bookRegisterFormDto.getUrl())
                .thumbnail(bookRegisterFormDto.getThumbnail())
                .source(bookRegisterFormDto.getType())
                .courseDetail(courseDetail)
                .build();

        courseBookRepository.save(courseBook);
    }

    public void modifyBook(Long courseDetailId, BookModifyFormDto bookModifyFormDto) {
        checkCourseDetailId(courseDetailId);
        CourseBook courseBook = getCourseBook(bookModifyFormDto.getId());

        courseBook.modifyBookInfo(bookModifyFormDto);
    }

    public void deleteBook(Long bookId) {
        CourseBook courseBook = getCourseBook(bookId);
        courseBookRepository.delete(courseBook);
    }

    private CourseBook getCourseBook(Long bookId) {
        return courseBookRepository.findById(bookId).orElseThrow(NotFoundCourseBookException::new);
    }

    private CourseVideo getCourseVideo(Long videoId) {
        return courseVideoRepository.findById(videoId).orElseThrow(NotFoundCourseVideoException::new);
    }

    private void checkCourseDetailId(Long courseDetailId) {
        courseDetailRepository.findById(courseDetailId).orElseThrow(() -> new CourseDetailNotFoundException("코스 상세를 찾을 수 없습니다."));
    }

    private String getDefaultYoutubeThumbnailIfAbsent(String thumbnail, VideoSource videoSource, String url) {
        if (Objects.equals(thumbnail, "") && videoSource == VideoSource.YOUTUBE) {
            return youtubeThumbnailMaker(url);
        }
        return thumbnail;
    }

    private String youtubeThumbnailMaker(String url) {
        return "https://img.youtube.com/vi/" + getYoutubeVideoId(url) + "/0.jpg";
    }

    private String getYoutubeVideoId(String url) {
        String videoId = null;
        String pattern = "v=([^&]+)";
        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(url);
        if (matcher.find()) {
            videoId = matcher.group(1);
        }
        return videoId;
    }
}
