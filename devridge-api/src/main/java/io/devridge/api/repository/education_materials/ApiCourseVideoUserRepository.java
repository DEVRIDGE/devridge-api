package io.devridge.api.repository.education_materials;

import io.devridge.core.domain.education_materials.video.CourseVideoUser;
import io.devridge.core.domain.education_materials.video.CourseVideoUserRepository;

import java.util.Optional;

public interface ApiCourseVideoUserRepository extends CourseVideoUserRepository {
    Optional<CourseVideoUser> findByCourseVideoIdAndUserId(Long CourseVideoId, Long UserId);

    void delete(CourseVideoUser entity);
}
