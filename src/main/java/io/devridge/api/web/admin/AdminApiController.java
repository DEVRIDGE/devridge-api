package io.devridge.api.web.admin;

import io.devridge.api.domain.companyinfo.Job;
import io.devridge.api.domain.companyinfo.JobRepository;
import io.devridge.api.domain.roadmap.*;
import io.devridge.api.dto.admin.*;
import io.devridge.api.dto.common.ApiResponse;
import io.devridge.api.handler.ex.CourseNotFoundException;
import io.devridge.api.handler.ex.JobNotFoundException;
import io.devridge.api.service.admin.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RequiredArgsConstructor
@RequestMapping("/admin/api")
@RestController
public class AdminApiController {

    private final CourseRepository courseRepository;
    private final CourseDetailRepository courseDetailRepository;
    private final AdminService adminService;
    private final JobRepository jobRepository;

    @GetMapping("/courses")
    public ResponseEntity<ApiResponse<CourseListDto>> getCourseList(@RequestParam("jobId") Long jobId) {
        List<Course> courseList = courseRepository.getCourseListByJob(jobId);

        return ResponseEntity.status(200).body(ApiResponse.success(new CourseListDto(courseList)));
    }

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
                .course(course)
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

    @PostMapping("/companyInfo")
    public ResponseEntity<ApiResponse<Object>> createCourse() {

        adminService.makeRoadmap();

        return ResponseEntity.status(200).body(ApiResponse.success("등록되었습니다."));
    }

    @PatchMapping("/requiredAbility")
    public ResponseEntity<ApiResponse<Object>> matchRequiredAbility() {

        adminService.matchRequiredAbilityWithCourseDetailId();
        return ResponseEntity.status(200).body(ApiResponse.success("등록되었습니다."));
    }
}
