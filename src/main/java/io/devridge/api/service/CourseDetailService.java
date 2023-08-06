package io.devridge.api.service;

import io.devridge.api.domain.course.Course;
import io.devridge.api.domain.course.CourseDetail;
import io.devridge.api.domain.course.CourseDetailRepository;
import io.devridge.api.domain.course.CourseRepository;
import io.devridge.api.domain.employment.*;
import io.devridge.api.dto.CourseDetailResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
//        List<EmploymentSkillCourseDetail> employmentSkillCourseDetails = new ArrayList<>();
//
//        Optional<List<EmploymentInfo>> employmentInfoOptional = employmentInfoRepository.findByCompanyIdAndJobId(companyId, jobId); // 특정 직무, 특정 회사에 대응되는 EmploymentInfo 리스트
//        List<EmploymentInfo> employmentInfos = employmentInfoOptional.get();
//
//        Optional<List<Course>> coursesMatchedWithJobOptional = courseRepository.findByIdAndJobId(courseId, jobId);// 특정 직무과 매칭 되는 Course 리스트
//        List<Course> coursesMatchedWithJob = coursesMatchedWithJobOptional.get(); // 특정 직무와 매칭되는 세부 기술들 저장
//
//        List<CourseDetail> totalCourseDetails = new ArrayList<>();
//        for(Course c : coursesMatchedWithJob){
//            Optional<List<CourseDetail>> courseDetails = courseDetailRepository.findByCourseId(c.getId());
//            totalCourseDetails.addAll(courseDetails.get());
//        }
//
//        /**
//         * 채용스킬과 대응되는 로드맵 상세기술을 찾아서 EmploymentSkillCourseDetailRepository에 저장하는 로직
//         */
//        for(EmploymentInfo ei : employmentInfos) {
//            Optional<List<EmploymentSkill>> employmentSkillsOptional = employmentSkillRepository.findByEmploymentInfoId(ei.getId());
//            List<EmploymentSkill> employmentSkills = employmentSkillsOptional.get();
//
//            for(EmploymentSkill es : employmentSkills) {
//                for(CourseDetail cd : totalCourseDetails) {
//                    if((es.getName().toLowerCase()).equals(cd.getName().toLowerCase())) {
//                        EmploymentSkillCourseDetail employmentSkillCourseDetail = new EmploymentSkillCourseDetail(es, cd);
//                        employmentSkillCourseDetails.add(employmentSkillCourseDetail);
//                        break;
//                    }
//                }
//            }
//        }
//
//        List<CourseDetail> courseDetailList = new ArrayList<>();
//        for(EmploymentSkillCourseDetail escd : employmentSkillCourseDetails) {
//            courseDetailList.add(escd.getCourseDetail());
//        }
//        String courseName = courseRepository.findById(courseId).get().getName();
//        return new CourseDetailResponseDto(courseName, courseDetailList);
        return null;
    }
}
