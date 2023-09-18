package io.devridge.api.dto.token;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReissueTokenRequestDto {
    private String token;

    @Builder
    public ReissueTokenRequestDto(String token) {
        this.token = token;
    }
}
