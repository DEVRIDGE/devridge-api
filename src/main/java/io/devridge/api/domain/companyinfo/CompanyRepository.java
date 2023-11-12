package io.devridge.api.domain.companyinfo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface CompanyRepository extends JpaRepository<Company, Long>  {

    @Query("select distinct c from Company c join fetch c.detailedPositionList")
    List<Company> findAllByFetch();
}
