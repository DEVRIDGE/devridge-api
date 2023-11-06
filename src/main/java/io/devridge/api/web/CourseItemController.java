package io.devridge.api.web;

import io.devridge.api.config.auth.LoginUser;
import io.devridge.api.dto.item.CourseItemResponseDto;
import io.devridge.api.dto.common.ApiResponse;
import io.devridge.api.dto.item.LikeCourseVideoRequestDto;
import io.devridge.api.service.CourseItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class CourseItemController {
    private final CourseItemService courseItemService;

    @GetMapping("/items")
    public ResponseEntity<ApiResponse<Object>> courseVideoList(
            @RequestParam("course") Long courseId,
            @RequestParam("courseDetail") Long courseDetailId,
            @RequestParam("company") Long companyId,
            @RequestParam("job") Long jobId,
            @RequestParam("detailedPosition") Long detailedPositionId,
            @AuthenticationPrincipal LoginUser loginUser) {

        CourseItemResponseDto courseVideoList = courseItemService.getCourseVideoAndBookList(courseId, courseDetailId, companyId, jobId, detailedPositionId, loginUser);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(courseVideoList));
    }

    @PostMapping("/videos/like")
    public ResponseEntity<ApiResponse<Object>> clickLikeOnCourseVideo(
            @Valid @RequestBody LikeCourseVideoRequestDto likeCourseVideoRequestDto,
            @AuthenticationPrincipal LoginUser loginUser) {

        boolean result = courseItemService.clickLikeOnCourseVideo(likeCourseVideoRequestDto, loginUser);

        if(result){
            return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("성공적으로 좋아요가 되었습니다."));
        }
        else {
            return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("성공적으로 좋아요 해제가 되었습니다."));
        }
    }
}
