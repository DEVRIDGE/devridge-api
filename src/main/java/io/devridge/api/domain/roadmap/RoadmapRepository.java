package io.devridge.api.domain.roadmap;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RoadmapRepository extends JpaRepository<Roadmap, Long> {

    @Query("SELECT r FROM Roadmap r " +
            "LEFT JOIN FETCH r.course c " +
            "WHERE r.companyInfo.id = :companyInfoId " +
            "ORDER BY c.order")
    List<Roadmap> getRoadmapsIncludingCoursesByCompanyInfoId(@Param("companyInfoId") Long companyInfoId);
}
