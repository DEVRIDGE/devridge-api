package io.devridge.api.domain.roadmap;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CourseDetailRepository extends JpaRepository<CourseDetail, Long> {
    @Query("SELECT cd FROM CourseDetail cd " +
            "JOIN CourseToDetail ctd ON cd.id = ctd.courseDetail.id " +
            "JOIN CompanyRequiredAbility cra ON cd.id = cra.courseDetail.id " +
            "JOIN CompanyInfoCompanyRequiredAbility cicra ON cra.id = cicra.companyRequiredAbility.id AND cicra.companyInfo.id = :companyInfoId " +
            "WHERE ctd.course.id = :courseId ORDER BY cd.name")
    List<CourseDetail> getCourseDetailListWithAbilityByCourseIdOrderByName(Long courseId, Long companyInfoId);


    @Query("SELECT cd FROM CourseDetail cd " +
            "JOIN CourseToDetail ctd ON cd.id = ctd.courseDetail.id " +
            "WHERE ctd.course.id = :courseId ORDER BY cd.name")
    List<CourseDetail> getAllCourseDetailByCourseIdOrderByName(Long courseId);

    Page<CourseDetail> findAllByOrderByName(Pageable pageable);

    @Query("SELECT cd FROM CourseDetail cd WHERE REPLACE(LOWER(cd.name), ' ', '') = LOWER(REPLACE(:name, ' ', ''))")
    Optional<CourseDetail> findByNameIgnoringCaseAndSpaces(String name);
}
