package io.devridge.api.service;

import io.devridge.api.domain.course.Course;
import io.devridge.api.domain.course.CourseDetail;
import io.devridge.api.domain.course.CourseDetailRepository;
import io.devridge.api.domain.course.CourseRepository;
import io.devridge.api.domain.employment.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class EmploymentService {
    private final CourseDetailRepository courseDetailRepository;
    private final CourseRepository courseRepository;

    private final EmploymentInfoRepository employmentInfoRepository;
    private final EmploymentSkillRepository employmentSkillRepository;
    private final EmploymentSkillCourseDetailRepository employmentSkillCourseDetailRepository;

    /**
     * 채용스킬과 대응되는 로드맵 상세기술을 찾아서 EmploymentSkillCourseDetailRepository에 저장하는 로직
     */
    public void saveEmploymentSkillCourseDetailRepository(long companyId, long jobId) {
        List<EmploymentSkillCourseDetail> employmentSkillAndCourseDetailList =
                employmentSkillCourseDetailRepository.findEmploymentSkillAndCourseDetailListByCompanyIdAndJobId(companyId, jobId);

        for(EmploymentSkillCourseDetail escd : employmentSkillAndCourseDetailList)  {
            employmentSkillCourseDetailRepository.save(escd);
        }
    }
}
