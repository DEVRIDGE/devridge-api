package io.devridge.api.domain.roadmap;

import io.devridge.api.domain.companyinfo.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {

    @Query("SELECT distinct c FROM Course c " +
            "JOIN FETCH c.courseToDetails " +
            "WHERE c.job = :job " +
            "ORDER BY c.order, CASE c.type WHEN 'SKILL' THEN 0 WHEN 'CS' THEN 1 ELSE 2 END")
    List<Course> getCourseByJobOrderByOrder(Job job);
}
