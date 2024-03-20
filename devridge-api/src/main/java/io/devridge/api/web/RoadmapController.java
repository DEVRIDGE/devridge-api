package io.devridge.api.web;

import io.devridge.api.config.auth.LoginUser;
import io.devridge.api.dto.roadmap.CourseDetailResponseDto;
import io.devridge.api.dto.common.ApiResponse;
import io.devridge.api.dto.roadmap.ChangeStudyStatusRequestDto;
import io.devridge.api.dto.roadmap.CourseItemResponseDto;
import io.devridge.api.dto.roadmap.RoadmapDto;
import io.devridge.api.service.roadmap.RoadmapService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequiredArgsConstructor
public class RoadmapController {

    private final RoadmapService roadmapService;

    @GetMapping("/courses")
    public ResponseEntity<ApiResponse<Object>> getCourseList(
            @RequestParam("job") Long jobId,
            @RequestParam("detailedPosition") Long detailedPositionId,
            @AuthenticationPrincipal LoginUser loginUser) {

        RoadmapDto courseList = roadmapService.getRoadmap(jobId, detailedPositionId, loginUser);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(courseList));
    }

    @GetMapping("/courses/{courseId}")
    public ResponseEntity<ApiResponse<Object>> courseDetailList(
            @PathVariable Long courseId,
            @RequestParam("job") Long jobId,
            @RequestParam("detailedPosition") Long detailedPositionId,
            @AuthenticationPrincipal LoginUser loginUser) {

        CourseDetailResponseDto courseDetailResponseDto = roadmapService.getCourseDetails(courseId, jobId, detailedPositionId, loginUser);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(courseDetailResponseDto));
    }

    @GetMapping("/courses/{courseId}/coursedetail/{courseDetailId}/items")
    public ResponseEntity<ApiResponse<CourseItemResponseDto>> courseItemList(
            @PathVariable Long courseId,
            @PathVariable Long courseDetailId,
            @RequestParam("job") Long jobId,
            @RequestParam("detailedPosition") Long detailedPositionId,
            @AuthenticationPrincipal LoginUser loginUser) {

        CourseItemResponseDto courseVideoList = roadmapService.getCourseVideoAndBookList(courseId, courseDetailId, jobId, detailedPositionId, loginUser);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(courseVideoList));
    }

    // TODO: roadmapId를 받아서 간단하게 변경하는 방식으로 변경하는게 좋을 것 같음
    @PostMapping("/courses/{courseId}/change-studystatus")
    public ResponseEntity<ApiResponse<Object>> changeStudyStatus(
            @PathVariable Long courseId,
            @Valid @RequestBody ChangeStudyStatusRequestDto changeStudyStatusRequestDto,
            @AuthenticationPrincipal LoginUser loginUser) {

        roadmapService.changeStudyStatus(courseId, changeStudyStatusRequestDto, loginUser);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("학습 상태를 성공적으로 변경하였습니다."));
    }
}
