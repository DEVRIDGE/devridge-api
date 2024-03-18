package io.devridge.api.service.company;

import io.devridge.api.dto.company.CompanyListDto;
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

    public CompanyListDto getCompanyList() {
        List<Company> companyList = companyRepository.findAll();

        return new CompanyListDto(companyList);
    }

    Optional<Company> findCompany(long companyId) {
        return companyRepository.findById(companyId);
    }
}
