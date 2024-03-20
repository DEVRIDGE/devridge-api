package io.devridge.api.repository.roadmap;

import io.devridge.api.dto.roadmap.RoadmapAndStatusDto;
import io.devridge.core.domain.roadmap.Roadmap;
import io.devridge.core.domain.roadmap.RoadmapRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ApiRoadmapRepository extends RoadmapRepository {
    @Query("SELECT new io.devridge.api.dto.roadmap.RoadmapAndStatusDto(r.matchingFlag, c, ur.studyStatus) FROM Roadmap r " +
            "JOIN r.course c " +
            "LEFT JOIN UserRoadmap ur ON ur.roadmap.id = r.id AND ur.user.id = :userId " +
            "WHERE r.recruitmentInfo.id = :recruitmentInfoId " +
            "ORDER BY c.order, CASE WHEN c.type = 'SKILL' THEN 0 WHEN c.type = 'CS' THEN 1 ELSE 2 END")
    List<RoadmapAndStatusDto> getRoadmapListByRecruitmentInfoIdAndUserId(@Param("recruitmentInfoId") Long recruitmentInfoId, @Param("userId") Long userId);

    @Query("SELECT r FROM Roadmap r " +
            "JOIN FETCH r.course c " +
            "WHERE r.course.id = :courseId AND r.recruitmentInfo.id = :recruitmentInfoId")
    Optional<Roadmap> findRoadmapWithCourseByCourseIdAndRecruitmentInfoId(@Param("courseId") Long courseId, @Param("recruitmentInfoId") Long recruitmentInfoId);

    List<Roadmap> findTop2ByRecruitmentInfoIdOrderByCourseOrder(Long recruitmentInfoId);

    Optional<Roadmap> findByCourseIdAndRecruitmentInfoId(Long courseId, Long recruitmentInfoId);
}
