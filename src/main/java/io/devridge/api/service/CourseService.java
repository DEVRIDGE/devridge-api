package io.devridge.api.service;

import io.devridge.api.config.auth.LoginUser;
import io.devridge.api.domain.companyinfo.CompanyInfo;
import io.devridge.api.domain.companyinfo.CompanyInfoRepository;
import io.devridge.api.domain.roadmap.*;
import io.devridge.api.dto.CourseDetailResponseDto;
import io.devridge.api.dto.course.CourseIndexList;
import io.devridge.api.dto.course.CourseInfoDto;
import io.devridge.api.dto.course.CourseListResponseDto;
import io.devridge.api.dto.course.RoadmapStatusDto;
import io.devridge.api.handler.ex.CompanyInfoNotFoundException;
import io.devridge.api.handler.ex.CourseNotFoundException;
import io.devridge.api.handler.ex.UnauthorizedCourseAccessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final CourseDetailRepository courseDetailRepository;
    private final CompanyInfoRepository companyInfoRepository;
    private final RoadmapRepository roadmapRepository;

    @Transactional(readOnly = true)
    public CourseListResponseDto getCourseList(long companyId, long jobId, long detailPositionId, LoginUser loginUser) {
        CompanyInfo companyInfo = findCompanyInfoWithCourse(companyId, jobId, detailPositionId);

        Long userId = getLoginUserId(loginUser);
        Collection<List<CourseInfoDto>> courseListCollection = getCourseListCollection(companyInfo, userId);

        List<CourseIndexList> courseList = addEmptyListIfSkillNextSkill(courseListCollection);

        return new CourseListResponseDto(companyInfo, courseList);
    }

    @Transactional(readOnly = true)
    public CourseDetailResponseDto getCourseDetailList(long courseId, long companyId, long jobId, long detailedPositionId, LoginUser loginUser) {
        CompanyInfo companyInfo = findCompanyInfo(companyId, jobId, detailedPositionId);
        Long userId = getLoginUserId(loginUser);
        /**
         * 로그인을 하지 않은 경우 접근이 허가된 코스인지 체크하고 허가되지 않은 경우 예외 발생
         */
        if (userId == null) {
            checkIfCourseIsAllowedForUnauthenticatedUser(courseId, companyInfo);
        }

        Course course = courseRepository.findById(courseId).orElseThrow(() -> new CourseNotFoundException("해당하는 코스가 없습니다."));
        List<CourseDetail> courseDetailList = courseDetailRepository.getCourseDetailList(courseId, companyId, jobId, detailedPositionId);

        return new CourseDetailResponseDto(course.getName(), courseDetailList);
    }


    private Long getLoginUserId(LoginUser loginUser) {
        return (loginUser != null) ? loginUser.getUser().getId() : null;
    }

    private CompanyInfo findCompanyInfoWithCourse(long companyId, long jobId, long detailPositionId) {
        return companyInfoRepository.findByCompanyIdAndJobIdAndDetailedPositionIdWithFetchJoin(companyId, jobId, detailPositionId)
                .orElseThrow(() -> new CompanyInfoNotFoundException("회사, 직무, 서비스에 일치 하는 회사 정보가 없습니다."));
    }

    private CompanyInfo findCompanyInfo(long companyId, long jobId, long detailPositionId) {
        return companyInfoRepository.findByCompanyIdAndJobIdAndDetailedPositionId(companyId, jobId, detailPositionId)
                .orElseThrow(() -> new CompanyInfoNotFoundException("회사, 직무, 서비스에 일치 하는 회사 정보가 없습니다."));
    }

    private Collection<List<CourseInfoDto>> getCourseListCollection(CompanyInfo companyInfo, Long userId) {
        List<RoadmapStatusDto> roadmapList = roadmapRepository.getRoadmapsIncludingCoursesByCompanyInfoIdWithUserId(companyInfo.getId(), userId);
        return roadmapList.stream()
                .map(CourseInfoDto::new)
                .collect(Collectors.groupingBy(CourseInfoDto::getOrder, TreeMap::new, Collectors.toList()))
                .values();
    }

    /**
     * 프론트 요구사항
     * 1. 컬렉션의 첫 번째 항목이 SKILL인 경우 빈 리스트를 추가
     * 2. 컬렉션의 이전 항목과 현재 항목이 모두 SKILL인 경우 빈 리스트 추가
     */
    private List<CourseIndexList> addEmptyListIfSkillNextSkill(Collection<List<CourseInfoDto>> courseListCollection) {
        List<CourseIndexList> courseListAddEmptyList = new ArrayList<>();
        int index = 0;
        CourseType previousCourseType = null;
        boolean isFirstItem = true;

        for (List<CourseInfoDto> courseInfoList : courseListCollection) {
            CourseType currentCourseType = !courseInfoList.isEmpty() ? courseInfoList.get(0).getType() : null;
            // 처음에만 실행
            if (isFirstItem) {
                isFirstItem = false;
                // 1. 컬렉션의 첫 번째 항목이 SKILL인 경우 빈 배열을 추가
                if (currentCourseType == CourseType.SKILL) {
                    courseListAddEmptyList.add(new CourseIndexList(index++, Collections.emptyList()));
                }
            }
            // 2. 이전 항목이 SKILL이고 현재 항목이 SKILL인 경우 빈 배열을 추가
            if (previousCourseType == CourseType.SKILL && currentCourseType == CourseType.SKILL) {
                courseListAddEmptyList.add(new CourseIndexList(index++, Collections.emptyList()));
            }
            // 원래 리스트 항목 추가
            courseListAddEmptyList.add(new CourseIndexList(index++, courseInfoList));
            previousCourseType = currentCourseType;
        }
        return courseListAddEmptyList;
    }

    private void checkIfCourseIsAllowedForUnauthenticatedUser(long courseId, CompanyInfo companyInfo) {
        boolean isAllowedCourse = roadmapRepository.findTop2ByCompanyInfoIdOrderByCourseOrder(companyInfo.getId())
                .stream().anyMatch(roadmap -> roadmap.getCourse().getId().equals(courseId));

        if (!isAllowedCourse) { throw new UnauthorizedCourseAccessException(); }
    }
}
