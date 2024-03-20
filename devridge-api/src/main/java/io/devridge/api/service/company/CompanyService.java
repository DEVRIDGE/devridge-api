package io.devridge.api.service.company;

import io.devridge.api.dto.company.CompanyListResponse;
import io.devridge.core.domain.company.Company;
import io.devridge.core.domain.company.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@Service
@Transactional
public class CompanyService {

    private final CompanyRepository companyRepository;

    public CompanyListResponse getCompanyList() {
        List<Company> companyList = companyRepository.findAll();

        return new CompanyListResponse(companyList);
    }

    Optional<Company> findCompany(long companyId) {
        return companyRepository.findById(companyId);
    }
}
