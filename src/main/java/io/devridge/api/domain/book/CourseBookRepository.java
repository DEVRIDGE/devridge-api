package io.devridge.api.domain.book;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CourseBookRepository extends JpaRepository<CourseBook, Long> {

    List<CourseBook> findByCourseDetailId(Long courseDetailId);

    @Query("SELECT cb FROM CourseBook cb " +
            "WHERE cb.courseDetail.id = :courseDetailId " +
            "ORDER BY (CASE WHEN cb.language = 'KOR' THEN 1 ELSE 2 END), cb.title ")
    List<CourseBook> findByCourseDetailIdOrderByLanguage(Long courseDetailId);
}
