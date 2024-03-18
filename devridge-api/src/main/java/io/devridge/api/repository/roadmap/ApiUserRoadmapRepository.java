package io.devridge.api.repository.roadmap;

import io.devridge.core.domain.user.UserRoadmap;
import io.devridge.core.domain.user.UserRoadmapRepository;

import java.util.Optional;

public interface ApiUserRoadmapRepository extends UserRoadmapRepository {
    Optional<UserRoadmap> findByUserIdAndRoadmapId(Long userId, Long roadmapId);
}
