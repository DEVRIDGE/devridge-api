package io.devridge.api.service;

import io.devridge.api.domain.roadmap.CourseDetail;
import io.devridge.api.domain.roadmap.CourseDetailRepository;
import io.devridge.api.domain.video.CourseVideo;
import io.devridge.api.domain.video.CourseVideoRepository;
import io.devridge.api.dto.CourseVideoResponseDto;
import io.devridge.api.handler.ex.CourseDetailNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseVideoService {
    private final CourseDetailRepository courseDetailRepository;
    private final CourseVideoRepository courseVideoRepository;

    @Transactional(readOnly = true)
    public CourseVideoResponseDto getCourseVideoList(long courseDetailId) {
        CourseDetail courseDetail = courseDetailRepository.findById(courseDetailId).orElseThrow(() -> new CourseDetailNotFoundException("해당하는 세부코스가 없습니다."));

        List<CourseVideo> courseVideoList = courseVideoRepository.findByCourseDetailIdOrderByLikeCntDesc(courseDetailId);

        return new CourseVideoResponseDto(courseDetail.getCourse().getName(), courseDetail.getName(), courseVideoList);
    }
}
