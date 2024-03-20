package io.devridge.admin.service.course;

import io.devridge.admin.repository.course.AdminCourseRepository;
import io.devridge.admin.repository.course.AdminRoadmapRepository;
import io.devridge.core.domain.company.Job;
import io.devridge.core.domain.recruitment.RecruitmentInfo;
import io.devridge.core.domain.recruitment.RecruitmentSkill;
import io.devridge.core.domain.roadmap.Course;
import io.devridge.core.domain.roadmap.MatchingFlag;
import io.devridge.core.domain.roadmap.Roadmap;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AdminRoadmapService {

    private final AdminCourseRepository courseRepository;
    private final AdminRoadmapRepository roadmapRepository;

    @Transactional
    public void makeRoadmap(Job job, RecruitmentInfo recruitmentInfo, List<RecruitmentSkill> recruitmentSkillList) {
        List<Course> courseList = courseRepository.getCourseByJobOrderByOrder(job);
        List<Roadmap> roadmaps = courseList.stream()
                .map(course -> {
                    boolean isMatch = course.getCourseToDetails().stream()
                            .anyMatch(courseToDetail -> recruitmentSkillList.stream()
                                    .anyMatch(ability -> ability.getCourseDetail() != null &&
                                            ability.getCourseDetail().getId().equals(courseToDetail.getCourseDetail().getId())));
                    return Roadmap.builder().course(course).recruitmentInfo(recruitmentInfo).matchingFlag(isMatch ? MatchingFlag.YES : MatchingFlag.NO).build();
                })
                .collect(Collectors.toList());
        roadmapRepository.saveAll(roadmaps);
    }
}
