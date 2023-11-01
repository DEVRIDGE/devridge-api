package io.devridge.api.service.admin;

import io.devridge.api.domain.roadmap.CourseDetailRepository;
import io.devridge.api.dto.admin.CourseDetailDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CourseDetailService {

    private final CourseDetailRepository courseDetailRepository;

    public List<CourseDetailDto> getAllCourseDetail() {
        return courseDetailRepository.findAllByOrderByName().stream().map(CourseDetailDto::new).collect(Collectors.toList());
    }
}
