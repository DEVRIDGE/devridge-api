package io.devridge.api.domain.company_job;

import io.devridge.api.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Long> {

}
