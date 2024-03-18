package io.devridge.api.repository.company;

import io.devridge.core.domain.company.Company;
import io.devridge.core.domain.company.CompanyJob;
import io.devridge.core.domain.company.CompanyJobRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ApiCompanyJobRepository extends CompanyJobRepository {

    @Query("SELECT cj FROM CompanyJob cj JOIN FETCH cj.job j WHERE cj.company = :company")
    List<CompanyJob> getCompanyJobByCompany(@Param("company") Company company);

    @Query("SELECT cj FROM CompanyJob cj WHERE cj.company.id = :companyId AND cj.job.id = :jobId")
    Optional<CompanyJob> findByCompanyIdAndJobId(@Param("companyId") long companyId, @Param("jobId") long jobId);
}
