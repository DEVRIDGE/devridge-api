package io.devridge.api.web;

import io.devridge.api.config.auth.LoginUser;
import io.devridge.api.dto.CourseVideoResponseDto;
import io.devridge.api.dto.common.ApiResponse;
import io.devridge.api.dto.coursevideo.LikeCourseVideoRequestDto;
import io.devridge.api.service.CourseVideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CourseVideoController {

    private final CourseVideoService courseVideoService;

    @GetMapping("/videos")
    public ResponseEntity<ApiResponse<Object>> courseVideoList(
            @RequestParam("course") Long courseId,
            @RequestParam("courseDetail") Long courseDetailId,
            @RequestParam("company") Long companyId,
            @RequestParam("job") Long jobId,
            @RequestParam("detailedPosition") Long detailedPositionId,
            @AuthenticationPrincipal LoginUser loginUser) {

        CourseVideoResponseDto courseVideoList = courseVideoService.getCourseVideoList(courseId, courseDetailId, companyId, jobId, detailedPositionId, loginUser);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(courseVideoList));
    }

    @PostMapping("/videos/like")
    public ResponseEntity<ApiResponse<Object>> clickLikeOnCourseVideo(
            @RequestBody LikeCourseVideoRequestDto likeCourseVideoRequestDto,
            @AuthenticationPrincipal LoginUser loginUser) {

        boolean result = courseVideoService.clickLikeOnCourseVideo(likeCourseVideoRequestDto, loginUser);

        if(result){
            return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("성공적으로 좋아요가 되었습니다."));
        }
        else {
            return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("성공적으로 좋아요 해제가 되었습니다."));
        }
    }
}
