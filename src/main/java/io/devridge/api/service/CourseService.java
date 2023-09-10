package io.devridge.api.service;

import io.devridge.api.domain.companyinfo.CompanyInfo;
import io.devridge.api.domain.companyinfo.CompanyInfoRepository;
import io.devridge.api.domain.companyinfo.CompanyJobRepository;
import io.devridge.api.domain.roadmap.*;
import io.devridge.api.dto.CourseDetailResponseDto;
import io.devridge.api.dto.course.CompanyJobInfo;
import io.devridge.api.dto.course.CourseIndexList;
import io.devridge.api.dto.course.CourseInfoDto;
import io.devridge.api.dto.course.CourseListResponseDto;
import io.devridge.api.handler.ex.CompanyJobNotFoundException;
import io.devridge.api.handler.ex.CourseNotFoundException;
import io.devridge.api.handler.ex.NotFoundCompanyInfo;
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
    private final CompanyInfoRepository companyInfoRepository;
    private final RoadmapRepository roadmapRepository;

    @Transactional(readOnly = true)
    public CourseListResponseDto getCourseList(long companyId, long jobId, long detailPositionId) {
        CompanyInfo companyInfo = findCompanyInfo(companyId, jobId, detailPositionId);
        List<Roadmap> roadmapList = roadmapRepository.getRoadmapsIncludingCoursesByCompanyInfoId(companyInfo.getId());
        System.out.println("roadmapList.get(0).getCourse().getName() = " + roadmapList.get(0).getCourse().getName());

        List<Course> courseList = courseRepository.getCourseListByJob(jobId);

        Collection<List<CourseInfoDto>> courseListCollection = groupCourseAndMakeNewList(courseList);
        List<CourseIndexList> courses = addEmptyListIfSkillNextSkill(courseListCollection);
        //return new CourseListResponseDto(companyJobInfo, courses);
        return null;
    }

    @Transactional(readOnly = true)
    public CourseDetailResponseDto getCourseDetailList(long courseId, long companyId, long jobId) {
        validateCompanyJob(companyId, jobId);

        Course course = courseRepository.findById(courseId).orElseThrow(() -> new CourseNotFoundException("해당하는 코스가 없습니다."));
        // TODO 쿼리수정
        List<CourseDetail> courseDetailList = null;
        //List<CourseDetail> courseDetailList = courseDetailRepository.getCourseDetailListByCourseIdAndCompanyIdAndJobId(courseId, companyId, jobId);

        return new CourseDetailResponseDto(course.getName(), courseDetailList);
    }

    private CompanyInfo findCompanyInfo(long companyId, long jobId, long detailPositionId) {
        return companyInfoRepository.findByCompanyIdAndJobIdAndDetailedPositionId(companyId, jobId, detailPositionId)
                .orElseThrow(NotFoundCompanyInfo::new);
    }

    private void validateCompanyJob(long companyId, long jobId) {
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
