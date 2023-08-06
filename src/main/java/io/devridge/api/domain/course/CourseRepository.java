package io.devridge.api.domain.course;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByJobId(Long jobId);
    List<Course> findByIdAndJobId(Long courseId, Long jobId);
}
