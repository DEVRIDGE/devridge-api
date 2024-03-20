package io.devridge.admin.repository.course;

import io.devridge.core.domain.roadmap.CourseDetail;
import io.devridge.core.domain.roadmap.CourseDetailRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AdminCourseDetailRepository extends CourseDetailRepository {

    Page<CourseDetail> findAllByOrderByName(Pageable pageable);

    @Query("SELECT cd FROM CourseDetail cd WHERE REPLACE(LOWER(cd.name), ' ', '') = LOWER(REPLACE(:name, ' ', ''))")
    Optional<CourseDetail> findByNameIgnoringCaseAndSpaces(@Param("name") String name);
}
