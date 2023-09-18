package io.devridge.api.domain.roadmap;

import io.devridge.api.dto.course.CourseDetailWithAbilityDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CourseDetailRepository extends JpaRepository<CourseDetail, Long> {

    @Query("SELECT cra.courseDetail.id " +
            "FROM CompanyRequiredAbility cra " +
            "JOIN cra.companyInfo ci " +
            "WHERE ci.company.id = :companyId AND ci.job.id = :jobId AND ci.detailedPosition.id = :detailedPositionId")
    List<Long> getMatchingCourseDetailIdsForCompanyAbility(Long companyId, Long jobId, Long detailedPositionId);

    @Query("SELECT new io.devridge.api.dto.course.CourseDetailWithAbilityDto(cd.id, cd.name, cra.id) " +
            "FROM CourseDetail cd " +
            "LEFT JOIN CompanyRequiredAbility cra ON cd.id = cra.courseDetail.id AND cra.courseDetail.id IN :filteredCourseDetailIds " +
            "WHERE cd.course.id = :courseId ORDER BY cd.name")
    List<CourseDetailWithAbilityDto> getCourseDetailListWithAbilityByCourseIdOrderByName(Long courseId, List<Long> filteredCourseDetailIds);
}
