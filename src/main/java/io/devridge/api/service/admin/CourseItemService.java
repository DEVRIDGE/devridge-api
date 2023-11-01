package io.devridge.api.service.admin;

import io.devridge.api.domain.book.CourseBook;
import io.devridge.api.domain.book.CourseBookRepository;
import io.devridge.api.domain.roadmap.CourseDetail;
import io.devridge.api.domain.roadmap.CourseDetailRepository;
import io.devridge.api.domain.video.CourseVideo;
import io.devridge.api.domain.video.CourseVideoRepository;
import io.devridge.api.domain.video.VideoSource;
import io.devridge.api.dto.admin.item.CourseItemListDto;
import io.devridge.api.dto.admin.item.VideoFormDto;
import io.devridge.api.handler.ex.CourseDetailNotFoundException;
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
public class CourseItemService {
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

    public void saveVideo(Long courseDetailId, VideoFormDto videoFormDto) {
        CourseDetail courseDetail = courseDetailRepository.findById(courseDetailId).orElseThrow(() -> new CourseDetailNotFoundException("코스 상세를 찾을 수 없습니다."));
        
        /*
        * 썸네일이 없고 유튜브인 경우 썸네일을 자동으로 만듬
         */
        if (Objects.equals(videoFormDto.getThumbnail(), "") && videoFormDto.getType() == VideoSource.YOUTUBE) {
            String thumbnailUrl = "https://img.youtube.com/vi/" +  getYoutubeVideoId(videoFormDto.getUrl()) + "/0.jpg";
            videoFormDto.setThumbnail(thumbnailUrl);
        }

        CourseVideo courseVideo = CourseVideo.builder()
                .title(videoFormDto.getTitle())
                .url(videoFormDto.getUrl())
                .owner(videoFormDto.getOwner())
                .thumbnail(videoFormDto.getThumbnail())
                .source(videoFormDto.getType())
                .courseDetail(courseDetail)
                .build();
        
        courseVideoRepository.save(courseVideo);
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
