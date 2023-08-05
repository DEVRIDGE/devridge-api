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
     * 특정 회사와 직무의 채용 스킬과 동일한 로드맵의 CourseDetail을
     * EmploymentSkillCourseDetail로 만들어 DB에 저장한다.
     */

    public void fillEmploymentSkillCourseDetailRepository(long companyId, long jobId) {
        HashMap<EmploymentSkill, CourseDetail> res = new LinkedHashMap<>();

        Optional<List<EmploymentInfo>> employmentInfoOptional = employmentInfoRepository.findByCompanyIdAndJobId(companyId, jobId); // 특정 직무, 특정 회사에 대응되는 EmploymentInfo 리스트
        List<EmploymentInfo> employmentInfos = employmentInfoOptional.get();

        Optional<List<Course>> coursesMatchedWithJobOptional = courseRepository.findByJobId(jobId);// 특정 직무과 매칭되는 Course 리스트
        List<Course> coursesMatchedWithJob = coursesMatchedWithJobOptional.get(); // 특정 직무와 매칭되는 세부 기술들 저장
        List<CourseDetail> totalCourseDetails = new ArrayList<>();

        for(Course c : coursesMatchedWithJob){
            Optional<List<CourseDetail>> courseDetails = courseDetailRepository.findByCourseId(c.getId());
            totalCourseDetails.addAll(courseDetails.get());
        }

        /**
         * 채용스킬과 대응되는 로드맵 상세기술을 찾아서 EmploymentSkillCourseDetailRepository에 저장하는 로직
         */
        for(EmploymentInfo ei : employmentInfos) {
            Optional<List<EmploymentSkill>> employmentSkillsOptional = employmentSkillRepository.findByEmploymentInfoId(ei.getId());
            List<EmploymentSkill> employmentSkills = employmentSkillsOptional.get();

            for(EmploymentSkill es : employmentSkills) {
                boolean Found = false;
                for(CourseDetail cd : totalCourseDetails) {
                    if((es.getName().toLowerCase()).equals(cd.getName().toLowerCase())) { // 소문자로 바꾼 채용 스킬 이름이 같으면 DB에 저장
                        Found = true;
                        EmploymentSkillCourseDetail employmentSkillCourseDetail = new EmploymentSkillCourseDetail(es, cd);
                        res.put(es, cd);
                        employmentSkillCourseDetailRepository.save(employmentSkillCourseDetail);
                        break;
                    }

                    if(!Found) {
                        // 특정 채용 스킬에 대응되는 로드맵 상세기술을 못찾았을 때 아무 작업도 안함,
                        // 따로 대응되지 못한 채용스킬들 저장소를 만들어서 저장한다든지 어떻게 대응 해야할지 고민해야함
                    }
                }
            }
        }
    }
}
