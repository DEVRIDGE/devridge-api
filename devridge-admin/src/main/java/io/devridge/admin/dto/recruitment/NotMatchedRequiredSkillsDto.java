package io.devridge.admin.dto.recruitment;

import lombok.Getter;

@Getter
public class NotMatchedRequiredSkillsDto {
    private Long id;
    private String name;

    public NotMatchedRequiredSkillsDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
