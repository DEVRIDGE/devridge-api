package io.devridge.api.domain.roadmap;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CourseToDetailRepository extends JpaRepository<CourseToDetail, Long> {
    @Query("SELECT ctd FROM CourseToDetail ctd " +
            "JOIN FETCH ctd.course " +
            "JOIN FETCH ctd.courseDetail " +
            "WHERE ctd.course.id = :courseId AND ctd.courseDetail.id = :courseDetailId")
    Optional<CourseToDetail> findFetchByCourseIdAndCourseDetailId(Long courseId, Long courseDetailId);
}
