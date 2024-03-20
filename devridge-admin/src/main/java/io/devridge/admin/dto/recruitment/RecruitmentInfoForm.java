package io.devridge.admin.dto.recruitment;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.List;

// TODO. companyInfoUrl -> 이름 변경 (프론트 변경이 필요)
// TODO. companyRequiredAbilityList -> 이름 변경 (프론트 변경이 필요)
@Getter
@NoArgsConstructor
public class RecruitmentInfoForm {

    @NotEmpty(message = "회사 이름 필드가 비어있습니다.")
    private String companyName;

    @NotEmpty(message = "직무 이름 필드가 비어있습니다.")
    private String jobName;

    @NotEmpty(message = "서비스 종류 이름 필드가 비어있습니다.")
    private String detailedPositionName;

    private String companyInfoUrl;

    private List<String> companyRequiredAbilityList;
}
