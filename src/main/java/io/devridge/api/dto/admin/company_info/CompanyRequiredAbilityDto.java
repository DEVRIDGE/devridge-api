package io.devridge.api.dto.admin.company_info;

import lombok.Getter;

@Getter
public class CompanyRequiredAbilityDto {
    private Long id;
    private String name;

    public CompanyRequiredAbilityDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
