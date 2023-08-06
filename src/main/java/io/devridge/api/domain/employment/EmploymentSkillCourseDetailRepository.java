package io.devridge.api.domain.employment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.*;

public interface EmploymentSkillCourseDetailRepository extends JpaRepository<EmploymentSkillCourseDetail, Long> {
    /**
     * 특정 회사와 직무의 채용 스킬과 동일한, 로드맵의 CourseDetail을 찾아 EmplymentSkillCourseDetail로 반환한다.
     */
    @Query("SELECT new EmploymentSkillCourseDetail(es, cd) "+
            "FROM Course c "+
            "LEFT JOIN CourseDetail cd ON cd.course.id = c.id and c.job.id = :jobId "+
            "RIGHT JOIN EmploymentSkill es ON LOWER(es.name)=LOWER(cd.name) "+
            "RIGHT JOIN EmploymentInfo ei ON ei.id = es.employmentInfo.id "+
            "WHERE ei.company.id = :companyId and ei.job.id = :jobId ")
    List<EmploymentSkillCourseDetail> findEmploymentSkillAndCourseDetailListByCompanyIdAndJobId(@Param("companyId")Long companyId, @Param("jobId") Long jobId);

}
