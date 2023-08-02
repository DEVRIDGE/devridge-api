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

    /**
     * 테스트용 코드
     */
//    @PostConstruct
//    public void init() {
//        Company company1 = new Company(1L, "test1", "test");
//        Company company2 = new Company(2L, "test2", "test");
//        companyRepository.save(company1);
//        companyRepository.save(company2);
//    }
}
