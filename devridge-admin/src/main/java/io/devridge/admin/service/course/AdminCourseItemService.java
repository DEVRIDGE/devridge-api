package io.devridge.admin.service.course;

import io.devridge.admin.dto.course.CourseItemListDto;
import io.devridge.admin.dto.item.BookModifyFormDto;
import io.devridge.admin.dto.item.BookRegisterFormDto;
import io.devridge.admin.dto.item.VideoModifyFormDto;
import io.devridge.admin.dto.item.VideoRegisterFormDto;
import io.devridge.admin.handler.common.ExceptionStatusCode;
import io.devridge.admin.handler.ex.AdminCourseDetailException.AdminNotFoundCourseDetailException;
import io.devridge.admin.handler.ex.AdminCourseItemException.AdminNotFoundCourseBookException;
import io.devridge.admin.handler.ex.AdminCourseItemException.AdminNotMatchedCourseDetailAndCourseVideoOfCourseDetailException;
import io.devridge.admin.handler.ex.AdminCourseItemException.AdminNotFoundCourseVideoException;
import io.devridge.admin.repository.course.AdminCourseBookRepository;
import io.devridge.admin.repository.course.AdminCourseDetailRepository;
import io.devridge.admin.repository.course.AdminCourseVideoRepository;
import io.devridge.core.domain.education_materials.book.CourseBook;
import io.devridge.core.domain.education_materials.book.CourseBookModify;
import io.devridge.core.domain.education_materials.video.CourseVideo;
import io.devridge.core.domain.education_materials.video.CourseVideoModify;
import io.devridge.core.domain.education_materials.video.VideoSource;
import io.devridge.core.domain.roadmap.CourseDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.devridge.admin.util.converter.CourseItemConverter.convertBookModifyFormDtoToCourseBookModify;
import static io.devridge.admin.util.converter.CourseItemConverter.convertVideoModifyFormDtoToCourseVideoModify;


@RequiredArgsConstructor
@Service
public class AdminCourseItemService {

    private final AdminCourseDetailRepository courseDetailRepository;
    private final AdminCourseVideoRepository courseVideoRepository;
    private final AdminCourseBookRepository courseBookRepository;

    @Transactional(readOnly = true)
    public CourseItemListDto findAllItemByCourseDetailId(Long courseDetailId) {
        CourseDetail courseDetail = findCourseDetail(courseDetailId);
        List<CourseVideo> courseVideos = findCourseVideos(courseDetailId);
        List<CourseBook> courseBooks = findCourseBooks(courseDetailId);

        return new CourseItemListDto(courseDetail, courseVideos, courseBooks);
    }

    @Transactional
    public void saveVideo(Long courseDetailId, VideoRegisterFormDto videoRegisterFormDto) {
        CourseDetail courseDetail = findCourseDetail(courseDetailId);
        setThumbnailIfSourceIsYoutube(videoRegisterFormDto);
        saveCourseVideo(courseDetail, videoRegisterFormDto);
    }

    @Transactional
    public void modifyVideo(Long courseDetailId, VideoModifyFormDto videoModifyFormDto) {
        checkCourseDetail(courseDetailId);
        CourseVideo courseVideo = findCourseVideo(videoModifyFormDto.getId(), courseDetailId, ExceptionStatusCode.COURSE_VIDEO_MODIFY);
        checkCourseDetailMatchCourseVideo(courseDetailId, courseVideo);

        modifyCourseVideo(courseVideo, videoModifyFormDto);
    }

    @Transactional
    public void deleteVideo(Long videoId, Long courseDetailId) {
        CourseVideo courseVideo = findCourseVideo(videoId, courseDetailId, ExceptionStatusCode.COURSE_VIDEO_DELETE);

        courseVideoRepository.delete(courseVideo);
    }

    @Transactional
    public void saveBook(Long courseDetailId, BookRegisterFormDto bookRegisterFormDto) {
        CourseDetail courseDetail = findCourseDetail(courseDetailId);
        saveCourseBook(courseDetail, bookRegisterFormDto);
    }

    @Transactional
    public void modifyBook(Long courseDetailId, BookModifyFormDto bookModifyFormDto) {
        checkCourseDetail(courseDetailId);
        CourseBook courseBook = findCourseBook(bookModifyFormDto.getId(), courseDetailId, ExceptionStatusCode.COURSE_BOOK_MODIFY);

        modifyCourseBook(courseBook, bookModifyFormDto);
    }

    @Transactional
    public void deleteBook(Long bookId, Long courseDetailId) {
        CourseBook courseBook = findCourseBook(bookId, courseDetailId, ExceptionStatusCode.COURSE_BOOK_DELETE);
        courseBookRepository.delete(courseBook);
    }

    private CourseDetail findCourseDetail(Long courseDetailId) {
        return courseDetailRepository.findById(courseDetailId)
                .orElseThrow(() -> new AdminNotFoundCourseDetailException(courseDetailId));
    }

    private List<CourseVideo> findCourseVideos(Long courseDetailId) {
        return courseVideoRepository.findByCourseDetailId(courseDetailId);
    }

    private List<CourseBook> findCourseBooks(Long courseDetailId) {
        return courseBookRepository.findByCourseDetailId(courseDetailId);
    }

    private void setThumbnailIfSourceIsYoutube(VideoRegisterFormDto videoRegisterFormDto) {
        if (Objects.equals(videoRegisterFormDto.getThumbnail(), "") && videoRegisterFormDto.getType() == VideoSource.YOUTUBE) {
            videoRegisterFormDto.setThumbnail(youtubeThumbnailMaker(videoRegisterFormDto.getUrl()));
        }
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

    private void saveCourseVideo(CourseDetail courseDetail, VideoRegisterFormDto videoRegisterFormDto) {
        CourseVideo courseVideo = videoRegisterFormDto.toDomain(courseDetail);
        courseVideoRepository.save(courseVideo);
    }

    private CourseVideo findCourseVideo(Long videoId, Long courseDetailId, String exceptionStatusCode) {
        return courseVideoRepository.findById(videoId).orElseThrow(() -> new AdminNotFoundCourseVideoException(videoId, courseDetailId, exceptionStatusCode));
    }

    private void checkCourseDetailMatchCourseVideo(Long courseDetailId, CourseVideo courseVideo) {
        Long courseDetailIdOfCourseVideo = courseVideo.getCourseDetail().getId();
        if (!courseDetailIdOfCourseVideo.equals(courseDetailId)) {
            throw new AdminNotMatchedCourseDetailAndCourseVideoOfCourseDetailException(courseDetailId, courseVideo.getId(), courseDetailIdOfCourseVideo);
        }
    }

    private void checkCourseDetail(Long courseDetailId) {
        courseDetailRepository.findById(courseDetailId)
                .orElseThrow(() -> new AdminNotFoundCourseDetailException(courseDetailId));
    }

    private void modifyCourseVideo(CourseVideo courseVideo, VideoModifyFormDto videoModifyFormDto) {
        CourseVideoModify courseVideoModify = convertVideoModifyFormDtoToCourseVideoModify(videoModifyFormDto);
        courseVideo.modifyCourseVideo(courseVideoModify);
    }

    private void saveCourseBook(CourseDetail courseDetail, BookRegisterFormDto bookRegisterFormDto) {
        CourseBook courseBook = bookRegisterFormDto.toDomain(courseDetail);
        courseBookRepository.save(courseBook);
    }

    private CourseBook findCourseBook(Long bookId, Long courseDetailId, String exceptionStatusCode) {
        return courseBookRepository.findById(bookId).orElseThrow(() -> new AdminNotFoundCourseBookException(bookId, courseDetailId, exceptionStatusCode));
    }

    private void modifyCourseBook(CourseBook courseBook, BookModifyFormDto bookModifyFormDto) {
        CourseBookModify courseBookModify = convertBookModifyFormDtoToCourseBookModify(bookModifyFormDto);
        courseBook.modifyCourseBook(courseBookModify);
    }
}
