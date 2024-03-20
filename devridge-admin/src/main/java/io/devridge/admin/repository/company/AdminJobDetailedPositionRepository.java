package io.devridge.admin.repository.company;

import io.devridge.core.domain.company.*;

import java.util.Optional;

public interface AdminJobDetailedPositionRepository extends JobDetailedPositionRepository {
    Optional<JobDetailedPosition> findByJobAndDetailedPosition(Job job, DetailedPosition detailedPosition);
}
