package io.devridge.api.dto.companyinfo;

import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
public class CompanyRequiredAbilityForm {
    private String companyName;

    private String jobName;

    private String detailedPositionName;

    List<String> companyRequiredAbilityList;
}
