package io.devridge.admin.repository.course;

import io.devridge.core.domain.education_materials.video.CourseVideo;
import io.devridge.core.domain.education_materials.video.CourseVideoRepository;

import java.util.List;

public interface AdminCourseVideoRepository extends CourseVideoRepository {

    List<CourseVideo> findByCourseDetailId(Long courseDetailId);
}
