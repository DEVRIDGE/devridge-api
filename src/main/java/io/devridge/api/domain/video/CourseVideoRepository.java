package io.devridge.api.domain.video;

import io.devridge.api.dto.coursevideo.CourseVideoWithLikeDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CourseVideoRepository extends JpaRepository<CourseVideo, Long> {
    //Long id, String title, String thumbnail, String url, VideoSource source, Long likeCnt
    @Query("SELECT new io.devridge.api.dto.coursevideo.CourseVideoWithLikeDto(cv.id, cv.title, cv.thumbnail, cv.url, cv.source, COUNT(cvu.id)) " +
            "FROM CourseVideo cv " +
            "LEFT JOIN CourseVideoUser cvu ON cv.id = cvu.courseVideo.id " +
            "WHERE cv.courseDetail.id = :courseDetailId " +
            "GROUP BY cv.id " +
            "ORDER BY COUNT(cvu.id) DESC")
    List<CourseVideoWithLikeDto> findWithLikeCntByCourseDetailIdOrderByLikeCntDesc(Long courseDetailId);

    List<CourseVideo> findByCourseDetailId(Long courseDetailId);
}
