package io.devridge.admin.repository.company;

import io.devridge.core.domain.company.Company;
import io.devridge.core.domain.company.CompanyRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AdminCompanyRepository extends CompanyRepository {

    @Query("select distinct c from Company c join fetch c.detailedPositionList")
    List<Company> findAllByFetch();

    @Query("select distinct c from Company c join fetch c.detailedPositionList where c.name = :companyName")
    Optional<Company> findByNameByFetch(@Param("companyName") String companyName);
}
