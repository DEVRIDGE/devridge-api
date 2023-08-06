package io.devridge.api.domain.employment;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmploymentSkillRepository extends JpaRepository<EmploymentSkill, Long> {
    public List<EmploymentSkill> findByEmploymentInfoId(Long employmentId);

}
