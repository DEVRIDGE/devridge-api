package io.devridge.api.dto.admin;

import io.devridge.api.domain.companyinfo.CompanyRequiredAbility;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class CompanyRequiredAbilityListByCourseDetailNullDto {
    List<CompanyRequiredAbilityDto> companyRequiredAbilityDtoList;

    public CompanyRequiredAbilityListByCourseDetailNullDto(List<CompanyRequiredAbility> companyRequiredAbilityList) {
        this.companyRequiredAbilityDtoList = companyRequiredAbilityList.stream()
                .map(CompanyRequiredAbilityDto::new)
                .collect(Collectors.toList());
    }

    @Getter
    public static class CompanyRequiredAbilityDto {
        private Long id;
        private String name;

        public CompanyRequiredAbilityDto(CompanyRequiredAbility companyRequiredAbility) {
            this.id = companyRequiredAbility.getId();
            this.name = companyRequiredAbility.getName();
        }
    }
}
