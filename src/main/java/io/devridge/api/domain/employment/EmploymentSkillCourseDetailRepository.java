package io.devridge.api.domain.employment;

import io.devridge.api.domain.company_job.Company;
import io.devridge.api.domain.course.CourseDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface EmploymentSkillCourseDetailRepository extends JpaRepository<EmploymentSkillCourseDetail, Long> {
//    @Query("SELECT DISTINCT new map(es, cd)"+
//            "FROM EmploymentInfo ei, EmploymentSkill es, CourseDetail cd, Course c "+
//            "WHERE cd.course_id = c.course_id "+
//            "and c.job_id = :jobId "+
//            "and LOWER(es.name)=LOWER(cd.name)"+
//            "and ei.company_id = :companyId and ei.job_id = :jobId "+
//            "and ei.employmeny_info_id = es.employment_info_id")
//    Optional<List<Map<EmploymentSkill, CourseDetail>>> test(@Param("companyId")Long companyId, @Param("jobId") Long jobId);
}
