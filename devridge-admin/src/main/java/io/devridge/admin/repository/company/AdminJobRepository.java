package io.devridge.admin.repository.company;

import io.devridge.core.domain.company.Job;
import io.devridge.core.domain.company.JobRepository;

import java.util.Optional;

public interface AdminJobRepository extends JobRepository {

    Optional<Job> findByName(String jobName);
}
