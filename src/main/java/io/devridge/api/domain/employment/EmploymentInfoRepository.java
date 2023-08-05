package io.devridge.api.domain.employment;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmploymentInfoRepository extends JpaRepository<EmploymentInfo, Long> {
    Optional<List<EmploymentInfo>> findByCompanyIdAndJobId(Long companyId, Long jobId);
}
