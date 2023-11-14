package io.devridge.api.domain.companyinfo;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JobDetailedPositionRepository extends JpaRepository<JobDetailedPosition, Long> {
    Optional<JobDetailedPosition> findByJobAndDetailedPosition(Job job, DetailedPosition detailedPosition);
}
