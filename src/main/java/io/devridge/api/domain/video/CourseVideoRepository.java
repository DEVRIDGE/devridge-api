package io.devridge.api.domain.video;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseVideoRepository extends JpaRepository<CourseVideo, Long> {

    List<CourseVideo> findByCourseDetailIdOrderByLikeCntDesc(Long courseDetailId);

    List<CourseVideo> findByCourseDetailId(Long courseDetailId);
}
