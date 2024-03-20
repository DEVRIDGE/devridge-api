package io.devridge.api.web;

import io.devridge.api.config.auth.LoginUser;
import io.devridge.api.dto.common.ApiResponse;
import io.devridge.api.dto.education_materials.LikeCourseVideoRequestDto;
import io.devridge.api.service.education_materials.EducationMaterialService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class EducationMaterialController {

    private final EducationMaterialService educationMaterialService;

    @PostMapping("/videos/like")
    public ResponseEntity<ApiResponse<Object>> clickLikeOnCourseVideo(
            @Valid @RequestBody LikeCourseVideoRequestDto likeCourseVideoRequestDto,
            @AuthenticationPrincipal LoginUser loginUser) {

        boolean result = educationMaterialService.clickLikeOnCourseVideo(likeCourseVideoRequestDto, loginUser);
        String message = result ? "성공적으로 좋아요가 되었습니다." : "성공적으로 좋아요 해제가 되었습니다.";

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(message));
    }
}
