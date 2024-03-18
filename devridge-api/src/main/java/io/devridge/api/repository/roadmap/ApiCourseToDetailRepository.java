package io.devridge.api.repository.roadmap;

import io.devridge.core.domain.roadmap.CourseToDetail;
import io.devridge.core.domain.roadmap.CourseToDetailRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ApiCourseToDetailRepository extends CourseToDetailRepository {
    @Query("SELECT ctd FROM CourseToDetail ctd " +
            "JOIN FETCH ctd.course " +
            "JOIN FETCH ctd.courseDetail " +
            "WHERE ctd.course.id = :courseId AND ctd.courseDetail.id = :courseDetailId")
    Optional<CourseToDetail> findFetchByCourseIdAndCourseDetailId(@Param("courseId") Long courseId, @Param("courseDetailId") Long courseDetailId);
}
