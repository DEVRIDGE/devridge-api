package io.devridge.api.service;

import io.devridge.api.domain.companyinfo.DetailedPositionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DetailedPositionService {
    private final DetailedPositionRepository detailedPositionRepository;

    //TODO DetailedPositionDto 만들고 서비스 로직 만들어야함
}
