package io.devridge.api.repository.education_materials;

import io.devridge.api.dto.education_materials.CourseVideoWithLikeDto;
import io.devridge.core.domain.education_materials.video.CourseVideo;
import io.devridge.core.domain.education_materials.video.CourseVideoRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ApiCourseVideoRepository extends CourseVideoRepository {
    /**
     * 특정 CourseDetail에 속한 CourseVideo들을 조회한다.
     * 정렬 순서는 다음과 같다.
     * 1. 해당 유저가 좋아요를 누른 영상이 제일 먼저 나온다.
     * 2. 좋아요 개수가 많은 순으로 나온다.
     * 3. 한국어 영상이 영어 영상보다 먼저 나온다.
     * 4. 영상 제목의 오름차순으로 나온다.
     */
    @Query("SELECT new io.devridge.api.dto.education_materials.CourseVideoWithLikeDto(cv.id, cv.title, cv.thumbnail, cv.url, cv.source, cvu.user.id, COUNT(cvu2.id)) " +
            "FROM CourseVideo cv " +
            "LEFT JOIN CourseVideoUser cvu ON cv.id = cvu.courseVideo.id AND cvu.user.id = :userId " +
            "LEFT JOIN CourseVideoUser cvu2 ON cv.id = cvu2.courseVideo.id " +
            "WHERE cv.courseDetail.id = :courseDetailId " +
            "GROUP BY cv.id " +
            "ORDER BY CASE WHEN cvu.user.id IS NOT NULL THEN 1 ELSE 0 END DESC, " +
            "COUNT(cvu2.id) DESC, " +
            "CASE WHEN cv.language = 'KOR' THEN 1 ELSE 2 END , " +
            "cv.title ")
    List<CourseVideoWithLikeDto> findWithLikeCntByCourseDetailIdOrderByLikeCntDesc(@Param("userId") Long userId, @Param("courseDetailId") Long courseDetailId);

    List<CourseVideo> findByCourseDetailId(@Param("courseDetailId") Long courseDetailId);
}
