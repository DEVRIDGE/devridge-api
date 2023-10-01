package io.devridge.api.web.admin;

import io.devridge.api.domain.companyinfo.*;
import io.devridge.api.domain.roadmap.Course;
import io.devridge.api.domain.roadmap.CourseDetail;
import io.devridge.api.domain.roadmap.CourseDetailRepository;
import io.devridge.api.domain.roadmap.CourseRepository;
import io.devridge.api.dto.admin.CompanyInfoDto;
import io.devridge.api.handler.ex.CourseNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/admin")
@Controller
public class AdminViewController {

    private final JobRepository jobRepository;
    private final CourseDetailRepository courseDetailRepository;
    private final CourseRepository courseRepository;
    private final CompanyInfoRepository companyInfoRepository;
    private final CompanyRequiredAbilityRepository companyRequiredAbilityRepository;

    @GetMapping
    public String adminMain() {
        return "main";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/course")
    public String courseList(Model model) {
        List<Job> jobList = jobRepository.findAll();
        model.addAttribute("jobList", jobList);

        return "courseList";
    }

    @GetMapping("/course/{courseId}")
    public String courseDetailList(@PathVariable Long courseId, Model model) {
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new CourseNotFoundException("코스를 찾을 수 없습니다."));
        List<CourseDetail> courseDetailList = courseDetailRepository.findByCourseIdOrderByName(courseId);
        model.addAttribute("course", course);
        model.addAttribute("courseDetailList", courseDetailList);

        return "courseDetailList";
    }

    @GetMapping("/companyInfo")
    public String companyInfo(Model model) {
        List<CompanyInfoDto> companyInfoList = companyInfoRepository.findByAllWithRoadmap();
        model.addAttribute("companyInfoList", companyInfoList);

        return "companyInfoList";
    }
    @GetMapping("/requiredability")
    public String requiredAbility(Model model) {
        List<Job> jobList = jobRepository.findAll();
        model.addAttribute("jobList", jobList);
        return "requiredAbilityList";
    }
}
