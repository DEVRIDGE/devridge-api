package io.devridge.api.domain.book;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseBookRepository extends JpaRepository<CourseBook, Long> {

    List<CourseBook> findByCourseDetailId(Long courseDetailId);
}
