package io.devridge.api.domain.employment;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmploymentInfoRepository extends JpaRepository<EmploymentInfo, Long> {
    List<EmploymentInfo> findByCompanyIdAndJobId(Long companyId, Long jobId);
}
