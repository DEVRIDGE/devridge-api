package io.devridge.api.domain.video;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CourseVideoUserRepository extends JpaRepository<CourseVideoUser, Long> {
    Optional<CourseVideoUser> findByCourseVideoIdAndUserId(Long CourseVideoId, Long UserId);

    void delete(CourseVideoUser entity);
}
