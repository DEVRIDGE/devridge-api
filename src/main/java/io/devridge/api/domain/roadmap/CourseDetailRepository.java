package io.devridge.api.domain.roadmap;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CourseDetailRepository extends JpaRepository<CourseDetail, Long> {

//    @Query("SELECT cd FROM CourseDetail cd " +
//            "LEFT JOIN EmploymentSkillCourseDetail escd ON cd.id = escd.courseDetail.id " +
//            "LEFT JOIN escd.employmentSkill es ON escd.employmentSkill.id = es.id " +
//            "LEFT JOIN es.employmentInfo ei ON es.employmentInfo.id = ei.id " +
//            "WHERE cd.course.id = :courseId AND ei.company.id = :companyId AND ei.job.id = :jobId")
//    List<CourseDetail> getCourseDetailListByCourseIdAndCompanyIdAndJobId(@Param("courseId")long courseId, @Param("companyId") long companyId, @Param("jobId") long jobId);
    CourseDetail findByName(String courseName);
}
