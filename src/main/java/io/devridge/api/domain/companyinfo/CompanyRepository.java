package io.devridge.api.domain.companyinfo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface CompanyRepository extends JpaRepository<Company, Long>  {

    @Query("select distinct c from Company c join fetch c.detailedPositionList")
    List<Company> findAllByFetch();

    @Query("select distinct c from Company c join fetch c.detailedPositionList where c.name = :companyName")
    Optional<Company> findByNameByFetch(String companyName);
}
