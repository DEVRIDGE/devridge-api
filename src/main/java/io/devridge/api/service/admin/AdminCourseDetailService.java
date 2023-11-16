package io.devridge.api.service.admin;

import io.devridge.api.domain.roadmap.CourseDetail;
import io.devridge.api.domain.roadmap.CourseDetailRepository;
import io.devridge.api.dto.admin.CourseDetailDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class AdminCourseDetailService {

    private final CourseDetailRepository courseDetailRepository;

    public Page<CourseDetailDto> getAllCourseDetail(Pageable pageable) {
        Page<CourseDetail> courseDetailPage = courseDetailRepository.findAllByOrderByName(pageable);
        return courseDetailPage.map(CourseDetailDto::new);
    }
}
