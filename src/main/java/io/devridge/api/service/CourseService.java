package io.devridge.api.service;

import io.devridge.api.domain.company_job.CompanyJobRepository;
import io.devridge.api.domain.course.*;
import io.devridge.api.domain.video.CourseVideo;
import io.devridge.api.domain.video.CourseVideoRepository;
import io.devridge.api.dto.CourseDetailResponseDto;
import io.devridge.api.dto.CourseVideoResponseDto;
import io.devridge.api.dto.course.CourseIndexList;
import io.devridge.api.dto.course.CourseInfoDto;
import io.devridge.api.dto.course.CourseListResponseDto;
import io.devridge.api.handler.ex.CompanyJobNotFoundException;
import io.devridge.api.handler.ex.CourseDetailNotFoundException;
import io.devridge.api.handler.ex.CourseNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final CompanyJobRepository companyJobRepository;
    private final CourseDetailRepository courseDetailRepository;
    private final CourseVideoRepository courseVideoRepository;

    @Transactional(readOnly = true)
    public CourseListResponseDto getCourseList(long companyId, long jobId) {
        validateCompanyJobExists(companyId, jobId);

        List<Course> courseList = courseRepository.getCourseListByJob(jobId);

        Collection<List<CourseInfoDto>> courseListCollection = groupCourseAndMakeNewList(courseList);
        List<CourseIndexList> courses = addEmptyListIfSkillNextSkill(courseListCollection);

        return new CourseListResponseDto(courses);
    }

    @Transactional(readOnly = true)
    public CourseDetailResponseDto getCourseDetailList(long courseId, long companyId, long jobId) {
        validateCompanyJobExists(companyId, jobId);

        Course courseName = courseRepository.findById(courseId).orElseThrow(() -> new CourseNotFoundException("해당하는 코스가 없습니다."));
        List<CourseDetail> courseDetailList = courseDetailRepository.getCourseDetailListByCourseIdAndCompanyIdAndJobId(courseId, companyId, jobId);


        return new CourseDetailResponseDto(courseName.getName(), courseDetailList);
    }

    @Transactional(readOnly = true)
    public CourseVideoResponseDto getCourseVideoList(long courseId, long courseDetailId, long companyId, long jobId) {
        validateCompanyJobExists(companyId, jobId);

        Course course = courseRepository.findById(courseId).orElseThrow(() -> new CourseNotFoundException("해당하는 코스가 없습니다."));
        CourseDetail courseDetail = courseDetailRepository.findById(courseDetailId).orElseThrow(() -> new CourseDetailNotFoundException("해당하는 세부코스가 없습니다."));


        List<CourseVideo> courseVideoList = courseVideoRepository.findByCourseDetailIdOrderByLikeCntDesc(courseDetailId);

        return new CourseVideoResponseDto(course.getName(), courseDetail.getName(), courseVideoList);
    }

    private void validateCompanyJobExists(long companyId, long jobId) {
        companyJobRepository.findByCompanyIdAndJobId(companyId, jobId)
                .orElseThrow(() -> new CompanyJobNotFoundException("회사와 직무에 일치 하는 정보가 없습니다."));
    }

    private Collection<List<CourseInfoDto>> groupCourseAndMakeNewList(List<Course> courseList) {
        return courseList.stream()
                .map(CourseInfoDto::new)
                .collect(Collectors.groupingBy(CourseInfoDto::getTurn, TreeMap::new, Collectors.toList()))
                .values();
    }

    private List<CourseIndexList> addEmptyListIfSkillNextSkill(Collection<List<CourseInfoDto>> courseListCollection) {
        List<CourseIndexList> courseListAddEmptyList = new ArrayList<>();
        int index = 0;
        CourseType previousCourseType = null;

        for (List<CourseInfoDto> courseInfoList : courseListCollection) {
            CourseType currentCourseType = !courseInfoList.isEmpty() ? courseInfoList.get(0).getType() : null;

            if (previousCourseType == CourseType.SKILL && currentCourseType == CourseType.SKILL) {
                courseListAddEmptyList.add(new CourseIndexList(index++, Collections.emptyList()));
            }
            courseListAddEmptyList.add(new CourseIndexList(index++, courseInfoList));

            previousCourseType = currentCourseType;
        }
        return courseListAddEmptyList;
    }
}
