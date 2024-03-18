package io.devridge.api.repository.roadmap;

import io.devridge.core.domain.roadmap.CourseDetail;
import io.devridge.core.domain.roadmap.CourseDetailRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ApiCourseDetailRepository extends CourseDetailRepository {
    @Query("SELECT cd FROM CourseDetail cd " +
            "JOIN CourseToDetail ctd ON cd.id = ctd.courseDetail.id " +
            "JOIN RecruitmentSkill rs ON cd.id = rs.courseDetail.id " +
            "JOIN RecruitmentInfoAndSkill rias ON rs.id = rias.recruitmentSkill.id AND rias.recruitmentInfo.id = :recruitmentInfoId " +
            "WHERE ctd.course.id = :courseId ORDER BY cd.name")
    List<CourseDetail> getCourseDetailListWithAbilityByCourseIdOrderByName(@Param("courseId") Long courseId, @Param("recruitmentInfoId") Long recruitmentInfoId);

    @Query("SELECT cd FROM CourseDetail cd " +
            "JOIN CourseToDetail ctd ON cd.id = ctd.courseDetail.id " +
            "WHERE ctd.course.id = :courseId ORDER BY cd.name")
    List<CourseDetail> getAllCourseDetailByCourseIdOrderByName(@Param("courseId") Long courseId);
}
