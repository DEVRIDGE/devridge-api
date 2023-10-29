package io.devridge.api.domain.video;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CourseVideoUserRepository extends JpaRepository<CourseVideoUser, Long> {
    Optional<CourseVideoUser> findByCourseVideoIdAndUserId(Long CourseVideoId, Long UserId);

    Long countByCourseVideoId(Long CourseVideoId);
    void delete(CourseVideoUser entity);
}
