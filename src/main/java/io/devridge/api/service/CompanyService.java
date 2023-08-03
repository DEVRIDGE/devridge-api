package io.devridge.api.service;

import io.devridge.api.domain.company_job.Company;
import io.devridge.api.domain.company_job.CompanyRepository;
import io.devridge.api.dto.CompanyResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CompanyService {
    private final CompanyRepository companyRepository;

    public CompanyResponseDto companyList() {
        List<Company> companyList = companyRepository.findAll();

        return new CompanyResponseDto(companyList);
    }
}
