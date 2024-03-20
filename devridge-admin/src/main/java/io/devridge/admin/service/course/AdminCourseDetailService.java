package io.devridge.admin.service.course;

import io.devridge.admin.dto.course.CourseDetailDto;
import io.devridge.admin.repository.course.AdminCourseDetailRepository;
import io.devridge.core.domain.roadmap.CourseDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AdminCourseDetailService {

    private final AdminCourseDetailRepository courseDetailRepository;

    public Page<CourseDetailDto> getAllCourseDetail(Pageable pageable) {
        Page<CourseDetail> courseDetailPage = courseDetailRepository.findAllByOrderByName(pageable);

        return courseDetailPage.map(CourseDetailDto::new);
    }

    public CourseDetail findByNameIgnoringSpace(String name) {
        return courseDetailRepository.findByNameIgnoringCaseAndSpaces(name).orElse(null);
    }
}
