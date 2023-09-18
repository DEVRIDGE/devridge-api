package io.devridge.api.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRoadmapRepository extends JpaRepository<UserRoadmap, Long> {

    Optional<UserRoadmap> findByUserIdAndRoadmapId(Long userId, Long roadmapId);
}
