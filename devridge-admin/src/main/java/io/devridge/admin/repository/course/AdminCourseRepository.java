package io.devridge.admin.repository.course;

import io.devridge.core.domain.company.Job;
import io.devridge.core.domain.roadmap.Course;
import io.devridge.core.domain.roadmap.CourseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AdminCourseRepository extends CourseRepository {
    @Query("SELECT distinct c FROM Course c " +
            "JOIN FETCH c.courseToDetails " +
            "WHERE c.job = :job " +
            "ORDER BY c.order, CASE WHEN c.type = 'SKILL' THEN 0 WHEN c.type = 'CS' THEN 1 ELSE 2 END")
    List<Course> getCourseByJobOrderByOrder(@Param("job") Job job);
}
