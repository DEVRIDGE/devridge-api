package io.devridge.api.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class DetailedPositionServiceTest {
    @InjectMocks
    private DetailedPositionService detailedPositionService;

    @DisplayName("회사와 직무가 주어지면 해당 detailedPostion 리스트를 정상적으로 반환한다")
    @Test
    public void getDetailedPositionList_success_test() {
        //given
        //stub
        //when
        //then
        throw new RuntimeException();
    }
}
