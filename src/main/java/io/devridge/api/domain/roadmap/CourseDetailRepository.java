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


    @Query("SELECT cd FROM CourseDetail cd " +
            "JOIN Course c ON c.id = cd.course.id " +
            "JOIN CompanyRequiredAbility cra ON cra.courseDetail.id = cd.id " +
            "JOIN CompanyInfo ci ON ci.id = cra.companyInfo.id " +
            "WHERE ci.company.id = :companyId AND ci.job.id = :jobId AND ci.detailedPosition.id = :detailedPositionId AND c.id = :courseId")
    List<CourseDetail> getCourseDetailList(@Param("courseId") Long courseId, @Param("companyId") Long companyId, @Param("jobId") Long jobId, @Param("detailedPostionId") Long detailedPositionId);
}
