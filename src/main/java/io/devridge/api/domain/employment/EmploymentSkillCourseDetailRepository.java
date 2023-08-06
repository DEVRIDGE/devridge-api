package io.devridge.api.domain.employment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.*;

public interface EmploymentSkillCourseDetailRepository extends JpaRepository<EmploymentSkillCourseDetail, Long> {
//    @Query("SELECT DISTINCT new map(es, cd)"+
//            "FROM EmploymentInfo ei, EmploymentSkill es, CourseDetail cd, Course c "+
//            "WHERE cd.id = c.id "+
//            "and c.job.id = :jobId "+
//            "and LOWER(es.name)=LOWER(cd.name)"+
//            "and ei.company.id = :companyId and ei.job.id = :jobId "+
//            "and ei.id = es.employmentInfo.id")
//    Optional<List<Map<EmploymentSkill, CourseDetail>>> test(@Param("companyId")Long companyId, @Param("jobId") Long jobId);

    /**
     * 특정 회사와 직무의 채용 스킬과 동일한, 로드맵의 CourseDetail을 찾아 EmplymentSkillCourseDetail로 반환한다.
     */
    @Query("SELECT new EmploymentSkillCourseDetail(es, cd) "+
            "FROM Course c "+
            "LEFT JOIN CourseDetail cd ON cd.course.id = c.id and c.job.id = :jobId "+
            "RIGHT JOIN EmploymentSkill es ON LOWER(es.name)=LOWER(cd.name) "+
            "RIGHT JOIN EmploymentInfo ei ON ei.id = es.employmentInfo.id "+
            "WHERE ei.company.id = :companyId and ei.job.id = :jobId ")
    ArrayList<EmploymentSkillCourseDetail> findEmploymentSkillAndCourseDetailListByCompanyIdAndJobId(@Param("companyId")Long companyId, @Param("jobId") Long jobId);

}
