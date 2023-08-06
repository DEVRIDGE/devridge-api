package io.devridge.api.domain.course;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CourseDetailRepository extends JpaRepository<CourseDetail, Long> {
    List<CourseDetail> findByCourseId(Long courseId);

    @Query("SELECT cd FROM CourseDetail cd " +
            "LEFT JOIN EmploymentSkillCourseDetail escd ON cd.id = escd.courseDetail.id " +
            "LEFT JOIN escd.employmentSkill es ON escd.employmentSkill.id = es.id " +
            "LEFT JOIN es.employmentInfo ei ON es.employmentInfo.id = ei.id " +
            "WHERE cd.course.id = ?1 AND ei.company.id = ?2 AND ei.job.id = ?3")
    List<CourseDetail> getCourseDetailListByCourseIdAndCompanyIdAndJobId(long courseId, long companyId, long jobId);
}
