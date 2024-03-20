package io.devridge.admin.repository.recruitment;

import io.devridge.core.domain.recruitment.RecruitmentSkill;
import io.devridge.core.domain.recruitment.RecruitmentSkillRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AdminRecruitmentSkillRepository extends RecruitmentSkillRepository {
    Page<RecruitmentSkill> findByCourseDetailIdIsNull(Pageable pageable);

    @Query("SELECT rs FROM RecruitmentSkill rs WHERE LOWER(REPLACE(rs.name, ' ', '')) = LOWER(REPLACE(:name, ' ', ''))")
    Optional<RecruitmentSkill> findByNameIgnoreCaseAndSpacesRemoved(@Param("name") String name);
}
