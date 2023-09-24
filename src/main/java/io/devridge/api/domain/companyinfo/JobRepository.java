package io.devridge.api.domain.companyinfo;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JobRepository extends JpaRepository<Job, Long> {
    Optional<Job> findByName(String jobName);
}
