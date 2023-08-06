package io.devridge.api.service;

import io.devridge.api.domain.course.CourseDetail;
import io.devridge.api.domain.course.CourseDetailRepository;
import io.devridge.api.domain.course.CourseRepository;
import io.devridge.api.domain.employment.*;
import io.devridge.api.dto.CourseDetailResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CourseDetailService {
    private final EmploymentInfoRepository employmentInfoRepository;
    private final EmploymentSkillRepository employmentSkillRepository;

    private final CourseRepository courseRepository;
    private final CourseDetailRepository courseDetailRepository;

    public CourseDetailResponseDto getCourseDetails(long courseId, long companyId, long jobId) {


        List<CourseDetail> courseDetailList = courseDetailRepository.getCourseDetailListByCourseIdAndCompanyIdAndJobId(courseId, companyId, jobId);
        String courseName = courseRepository.findById(courseId).get().getName();
        return new CourseDetailResponseDto(courseName, courseDetailList);
    }
}
