package io.devridge.api.service;


import io.devridge.api.domain.companyinfo.Company;
import io.devridge.api.domain.companyinfo.CompanyRepository;
import io.devridge.api.dto.CompanyResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CompanyService {
    private final CompanyRepository companyRepository;

    public CompanyResponseDto companyList() {
        List<Company> companyList = companyRepository.findAll();

        return new CompanyResponseDto(companyList);
    }

    public Optional<Company> findByName(String name) {
        return companyRepository.findByName(name);
    }

    public Company save(Company company) {
        return companyRepository.save(company);
    }
}
