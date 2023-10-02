package io.devridge.api.domain.companyinfo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface JobRepository extends JpaRepository<Job, Long> {
    Optional<Job> findByName(String jobName);

    @Query("SELECT  j " +
            "FROM Job j " +
            "JOIN CompanyJob cj ON j.id = cj.job.id " +
            "WHERE cj.company.id = :companyId")
    List<Job> findByCompanyId(Long companyId);
}
