package io.devridge.api.repository.education_materials;

import io.devridge.core.domain.education_materials.book.CourseBook;
import io.devridge.core.domain.education_materials.book.CourseBookRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ApiCourseBookRepository extends CourseBookRepository {
    @Query("SELECT cb FROM CourseBook cb " +
            "WHERE cb.courseDetail.id = :courseDetailId " +
            "ORDER BY (CASE WHEN cb.language = 'KOR' THEN 1 ELSE 2 END), cb.title ")
    List<CourseBook> findByCourseDetailIdOrderByLanguage(@Param("courseDetailId") Long courseDetailId);
}
