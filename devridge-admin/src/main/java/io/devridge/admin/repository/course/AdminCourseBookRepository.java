package io.devridge.admin.repository.course;

import io.devridge.core.domain.education_materials.book.CourseBook;
import io.devridge.core.domain.education_materials.book.CourseBookRepository;

import java.util.List;

public interface AdminCourseBookRepository extends CourseBookRepository {

    List<CourseBook> findByCourseDetailId(Long courseDetailId);
}
