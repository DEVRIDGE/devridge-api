package io.devridge.api.service;

import io.devridge.api.domain.company_job.CompanyJobRepository;
import io.devridge.api.domain.course.Course;
import io.devridge.api.domain.course.CourseRepository;
import io.devridge.api.dto.course.CourseListResponseDto;
import io.devridge.api.handler.ex.CompanyJobNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CourseService {

    private final CourseRepository courseJpaRepository;
    private final CompanyJobRepository companyJobRepository;

    @Transactional(readOnly = true)
    public CourseListResponseDto getCourseList(long companyId, long jobId) {
        validateCompanyJobExists(companyId, jobId);

        List<Course> courseList = courseJpaRepository.getCourseListByJob(jobId);

        return new CourseListResponseDto(courseList);
    }

    private void validateCompanyJobExists(long companyId, long jobId) {
        companyJobRepository.findByCompanyIdAndJobId(companyId, jobId)
                .orElseThrow(() -> new CompanyJobNotFoundException("회사와 직무에 일치 하는 정보가 없습니다."));
    }
}
