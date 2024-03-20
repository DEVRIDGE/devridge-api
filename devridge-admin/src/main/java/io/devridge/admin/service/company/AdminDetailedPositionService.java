package io.devridge.admin.service.company;

import io.devridge.admin.repository.company.AdminDetailedPositionRepository;
import io.devridge.core.domain.company.Company;
import io.devridge.core.domain.company.DetailedPosition;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class AdminDetailedPositionService {

    private final AdminDetailedPositionRepository detailedPositionRepository;

    @Transactional
    public DetailedPosition saveDetailPosition(String detailedPositionName, Company company) {
        DetailedPosition detailedPosition = DetailedPosition.builder().name(detailedPositionName).company(company).build();
        return detailedPositionRepository.save(detailedPosition);
    }
}
