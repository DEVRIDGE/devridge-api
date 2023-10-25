package io.devridge.api.web.admin;

import io.devridge.api.domain.companyinfo.CompanyRequiredAbility;
import io.devridge.api.domain.companyinfo.CompanyRequiredAbilityRepository;
import io.devridge.api.domain.companyinfo.Job;
import io.devridge.api.domain.companyinfo.JobRepository;
import io.devridge.api.domain.roadmap.*;
import io.devridge.api.domain.video.CourseVideo;
import io.devridge.api.domain.video.CourseVideoRepository;
import io.devridge.api.domain.video.VideoSource;
import io.devridge.api.dto.admin.*;
import io.devridge.api.dto.common.ApiResponse;
import io.devridge.api.handler.ex.CourseNotFoundException;
import io.devridge.api.handler.ex.JobNotFoundException;
import io.devridge.api.service.admin.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@RequiredArgsConstructor
@RequestMapping("/admin/api")
@RestController
public class AdminApiController {

    private final CourseRepository courseRepository;
    private final CourseDetailRepository courseDetailRepository;
    private final AdminService adminService;
    private final JobRepository jobRepository;
    private final CompanyRequiredAbilityRepository companyRequiredAbilityRepository;
    private final CourseVideoRepository courseVideoRepository;

//    @GetMapping("/courses")
//    public ResponseEntity<ApiResponse<CourseListDto>> getCourseList(@RequestParam("jobId") Long jobId) {
//        List<Course> courseList = courseRepository.getCourseListByJob(jobId);
//
//        return ResponseEntity.status(200).body(ApiResponse.success(new CourseListDto(courseList)));
//    }

    @PostMapping("/course")
    public ResponseEntity<ApiResponse<Object>> createCourse(@RequestBody @Valid CourseCreateInfo courseCreateInfo) {
        Job job = jobRepository.findById(courseCreateInfo.getJobId()).orElseThrow(JobNotFoundException::new);
        Course course = Course.builder().
                name(courseCreateInfo.getName()).
                type(CourseType.valueOf(courseCreateInfo.getType())).
                order(courseCreateInfo.getOrder()).
                job(job).
                build();

        courseRepository.save(course);

        return ResponseEntity.status(200).body(ApiResponse.success("등록되었습니다."));
    }

    @PutMapping("/course")
    public ResponseEntity<ApiResponse<Object>> editCourse(@RequestBody @Valid CourseInfo courseInfo) {
        adminService.changeCourse(courseInfo);
        return ResponseEntity.status(200).body(ApiResponse.success("수정되었습니다."));
    }

    @DeleteMapping("/course/{courseId}")
    public ResponseEntity<ApiResponse<Object>> deleteCourse(@PathVariable Long courseId) {

        adminService.deleteCourse(courseId);

        return ResponseEntity.status(200).body(ApiResponse.success("삭제되었습니다."));
    }

    @PostMapping("/courseDetail")
    public ResponseEntity<ApiResponse<Object>> createCourse(@RequestBody @Valid CourseDetailCreateInfo courseDetailCreateInfo) {
        Course course = courseRepository.findById(courseDetailCreateInfo.getCourseId()).orElseThrow(() -> new CourseNotFoundException("코스를 찾을 수 없습니다."));
        CourseDetail courseDetail = CourseDetail.builder()
                .name(courseDetailCreateInfo.getName())
                .build();

        courseDetailRepository.save(courseDetail);

        return ResponseEntity.status(200).body(ApiResponse.success("등록되었습니다."));
    }

    @PutMapping("/courseDetail")
    public ResponseEntity<ApiResponse<Object>> editCourseDetail(@RequestBody @Valid CourseDetailInfo courseDetailInfo) {
        adminService.changeCourseDetail(courseDetailInfo);
        return ResponseEntity.status(200).body(ApiResponse.success("수정되었습니다."));
    }

    @DeleteMapping("/courseDetail/{courseDetailId}")
    public ResponseEntity<ApiResponse<Object>> deleteCourseDetail(@PathVariable Long courseDetailId) {
        adminService.deleteCourseDetail(courseDetailId);
        return ResponseEntity.status(200).body(ApiResponse.success("삭제되었습니다."));
    }

//    @PostMapping("/companyInfo")
//    public ResponseEntity<ApiResponse<Object>> createCourse() {
//
//        adminService.makeRoadmap();
//
//        return ResponseEntity.status(200).body(ApiResponse.success("등록되었습니다."));
//    }

//    @GetMapping("/requiredAbility/{jobId}")
//    public ResponseEntity<ApiResponse<Object>> getRequiredAbilityWithNotHaveCourseId(@PathVariable Long jobId) {
//        List<CompanyRequiredAbility> companyRequiredAbilityList = companyRequiredAbilityRepository.findAllByCourseDetailIsNullFetch(jobId);
//
//        return ResponseEntity.status(200).body(ApiResponse.success("요청 성공", companyRequiredAbilityList));
//    }

//    @PatchMapping("/requiredAbility/{jobId}")
//    public ResponseEntity<ApiResponse<Object>> matchRequiredAbility(@PathVariable Long jobId) {
//
//        adminService.matchRequiredAbilityWithCourseDetailId(jobId);
//        return ResponseEntity.status(200).body(ApiResponse.success("등록되었습니다."));
//    }

    @DeleteMapping("/video/{videoId}")
    public ResponseEntity<ApiResponse<Object>> courseVideoDelete(@PathVariable Long videoId) {
        courseVideoRepository.deleteById(videoId);

        return ResponseEntity.status(200).body(ApiResponse.success("삭제되었습니다.."));
    }

    @PostMapping("/video/{courseDetailId}")
    public ResponseEntity<ApiResponse<Object>> createCourseVideo(@PathVariable Long courseDetailId,
                                                                 @RequestParam String name,
                                                                 @RequestParam String url,
                                                                 @RequestParam String owner,
                                                                 @RequestParam Integer likes) {
        CourseDetail courseDetail = courseDetailRepository.findById(courseDetailId).orElseThrow(() -> new CourseNotFoundException("코스를 찾을 수 없습니다."));


        String videoId = extractVideoId(url);
        if (videoId == null) {
            throw new RuntimeException("에러");
        }
        String thumbnail = "https://img.youtube.com/vi/" + videoId + "/0.jpg";


        CourseVideo courseVideo = CourseVideo.builder()
                .title(name)
                .url(url)
                .thumbnail(thumbnail)
                .owner(owner)
                .source(VideoSource.YOUTUBE)
                .likeCnt(likes)
                .courseDetail(courseDetail)
                .build();

        courseVideoRepository.save(courseVideo);

        return ResponseEntity.status(200).body(ApiResponse.success("등록되었습니다."));
    }

    private static final Pattern YOUTUBE_VIDEO_ID_PATTERN = Pattern.compile("v=([a-zA-Z0-9_\\-]+)");
    private static String extractVideoId(String youtubeUrl) {
        Matcher matcher = YOUTUBE_VIDEO_ID_PATTERN.matcher(youtubeUrl);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }
}
