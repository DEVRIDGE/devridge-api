package io.devridge.api.domain.course;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CourseDetailRepository extends JpaRepository<CourseDetail, Long> {
    Optional<List<CourseDetail>> findByCourseId(Long courseId);
}
