package io.devridge.api.web;

import io.devridge.api.dto.common.ApiResponse;
import io.devridge.api.service.DetailedPositionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DetailedPositionController {
    private final DetailedPositionService detailedPositionService;

    @GetMapping("/detailedPositions")
    public ResponseEntity<ApiResponse<Object>> detailedPositionList() {

        return null;
    }
}
