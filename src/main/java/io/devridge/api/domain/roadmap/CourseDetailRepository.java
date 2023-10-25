package io.devridge.api.domain.roadmap;

import io.devridge.api.dto.course.CourseDetailWithAbilityDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CourseDetailRepository extends JpaRepository<CourseDetail, Long> {

    @Query("SELECT cra.courseDetail.id " +
            "FROM CompanyRequiredAbility cra " +
            "WHERE cra.companyInfo.id = :companyInfoId")
    List<Long> getMatchingCourseDetailIdsForCompanyAbility(Long companyInfoId);

    @Query("SELECT new io.devridge.api.dto.course.CourseDetailWithAbilityDto(cd.id, cd.name, cra.id) " +
            "FROM CourseDetail cd " +
            "LEFT JOIN CourseToDetail ctd ON cd.id = ctd.courseDetail.id " +
            "LEFT JOIN CompanyRequiredAbility cra ON cd.id = cra.courseDetail.id AND cra.companyInfo.id = :companyInfoId AND cra.courseDetail.id IN :filteredCourseDetailIds " +
            "WHERE ctd.course.id = :courseId ORDER BY cd.name")
    List<CourseDetailWithAbilityDto> getCourseDetailListWithAbilityByCourseIdOrderByName(Long courseId, Long companyInfoId, List<Long> filteredCourseDetailIds);
}
