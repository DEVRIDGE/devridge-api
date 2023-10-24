package io.devridge.api.domain.roadmap;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {

    @Query("SELECT c FROM Course c WHERE c.job.id = :jobId ORDER BY c.order, CASE c.type WHEN 'SKILL' THEN 0 WHEN 'CS' THEN 1 ELSE 2 END")
    List<Course> getCourseListByJob(@Param("jobId") long jobId);

    List<Course> findByJobId(Long jobId);

    List<Course> findByJobIdOrderByOrder(Long jobId);
}
