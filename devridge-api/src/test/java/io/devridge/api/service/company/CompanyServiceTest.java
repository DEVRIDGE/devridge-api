package io.devridge.api.service.company;

import io.devridge.api.dto.company.CompanyListResponse;
import io.devridge.core.domain.company.Company;
import io.devridge.core.domain.company.CompanyRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CompanyServiceTest {

    @InjectMocks
    private CompanyService companyService;

    @Mock
    private CompanyRepository companyRepository;

    @DisplayName("회사 리스트 조회에 성공합니다.")
    @Test
    void getCompanyList_success_test() {
        // given
        List<Company> companies = new ArrayList<>();
        companies.add(Company.builder().id(1L).name("company1").build());
        companies.add(Company.builder().id(2L).name("company2").build());

        // stub
        when(companyRepository.findAll()).thenReturn(companies);

        // when
        CompanyListResponse companyList = companyService.getCompanyList();

        // then
        assertThat(companyList.getCompanies().size()).isEqualTo(2);
        assertThat(companyList.getCompanies().get(0).getId()).isEqualTo(1L);
        assertThat(companyList.getCompanies().get(0).getName()).isEqualTo("company1");
        assertThat(companyList.getCompanies().get(1).getId()).isEqualTo(2L);
        assertThat(companyList.getCompanies().get(1).getName()).isEqualTo("company2");
    }

    @DisplayName("빈 회사 리스트 조회에 성공합니다.")
    @Test
    void getCompanyList_empty_test() {
        // given
        List<Company> companies = new ArrayList<>();

        // stub
        when(companyRepository.findAll()).thenReturn(companies);

        // when
        CompanyListResponse companyList = companyService.getCompanyList();

        // then
        assertThat(companyList.getCompanies().size()).isEqualTo(0);
    }

    @DisplayName("회사 조회에 성공합니다.")
    @Test
    void findCompany_success_test() {
        // given
        Company company = Company.builder().id(1L).name("company1").build();

        // stub
        when(companyRepository.findById(1L)).thenReturn(java.util.Optional.of(company));

        // when
        Optional<Company> result = companyService.findCompany(1L);

        // then
        assertThat(result.get().getId()).isEqualTo(1L);
        assertThat(result.get().getName()).isEqualTo("company1");
    }

    @DisplayName("회사 조회시 회사가 없을 때 빈 Optional을 반환합니다.")
    @Test
    void findCompany_fail_test() {
        // stub
        when(companyRepository.findById(1L)).thenReturn(Optional.empty());

        // when
        Optional<Company> result = companyService.findCompany(1L);

        // then
        assertThat(result).isEmpty();
    }
}