package io.devridge.api.service;

import io.devridge.api.config.auth.LoginUser;
import io.devridge.api.domain.companyinfo.CompanyInfo;
import io.devridge.api.domain.companyinfo.CompanyInfoRepository;
import io.devridge.api.domain.roadmap.*;
import io.devridge.api.domain.user.LoginStatus;
import io.devridge.api.domain.user.UserRoadmapRepository;
import io.devridge.api.dto.CourseDetailResponseDto;
import io.devridge.api.dto.course.*;
import io.devridge.api.handler.ex.CompanyInfoNotFoundException;
import io.devridge.api.handler.ex.RoadmapNotMatchCourseAndCompanyInfoException;
import io.devridge.api.handler.ex.UnauthorizedCourseAccessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CourseService {

    private final CourseDetailRepository courseDetailRepository;
    private final CompanyInfoRepository companyInfoRepository;
    private final RoadmapRepository roadmapRepository;
    private final UserRoadmapRepository userRoadmapRepository;

    @Transactional(readOnly = true)
    public CourseListResponseDto getCourseList(long companyId, long jobId, long detailedPositionId, LoginUser loginUser) {
        CompanyInfo companyInfo = findCompanyInfoWithCompanyAndJob(companyId, jobId, detailedPositionId);

        Long userId = getLoginUserId(loginUser);
        Collection<List<CourseInfoDto>> courseListCollection = getCourseListCollection(companyInfo, userId);

        List<CourseIndexList> courseList = addEmptyListIfSkillNextSkill(courseListCollection);

        return new CourseListResponseDto(companyInfo, courseList);
    }

    @Transactional(readOnly = true)
    public CourseDetailResponseDto getCourseDetailList(long courseId, long companyId, long jobId, long detailedPositionId, LoginUser loginUser) {
        CompanyInfo companyInfo = findCompanyInfo(companyId, jobId, detailedPositionId);
        Roadmap roadmap = getRoadmapWithCourseByCompanyInfo(courseId, companyInfo);
        checkCourseAccessForUser(getLoginUserId(loginUser), roadmap.getId(), companyInfo);

        List<CourseDetailWithAbilityDto> courseDetailList = getFilteredOrAllCourseDetails(companyInfo, courseId);

        return new CourseDetailResponseDto(roadmap.getCourse().getName(), getUserStudyStatus(loginUser, roadmap), courseDetailList);
    }

    private UserStudyStatusDto getUserStudyStatus(LoginUser loginUser, Roadmap roadmap) {
        if (loginUser == null) {
            return UserStudyStatusDto.builder().loginStatus(LoginStatus.NO).build();
        }
        return userRoadmapRepository.findByUserIdAndRoadmapId(getLoginUserId(loginUser), roadmap.getId())
                .map(userRoadmap -> UserStudyStatusDto.builder().loginStatus(LoginStatus.YES).studyStatus(userRoadmap.getStudyStatus()).build())
                .orElse(UserStudyStatusDto.builder().loginStatus(LoginStatus.YES).build());
    }

    private Long getLoginUserId(LoginUser loginUser) {
        return (loginUser != null) ? loginUser.getUser().getId() : null;
    }

    private CompanyInfo findCompanyInfoWithCompanyAndJob(long companyId, long jobId, long detailedPositionId) {
        return companyInfoRepository.findByCompanyIdAndJobIdAndDetailedPositionIdWithFetchJoin(companyId, jobId, detailedPositionId)
                .orElseThrow(() -> new CompanyInfoNotFoundException("회사, 직무, 서비스에 일치 하는 회사 정보가 없습니다."));
    }

    private CompanyInfo findCompanyInfo(long companyId, long jobId, long detailedPositionId) {
        return companyInfoRepository.findByCompanyIdAndJobIdAndDetailedPositionId(companyId, jobId, detailedPositionId)
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

    /**
     * 로그인을 하지 않은 경우 접근이 허가된 코스인지 체크하고 허가되지 않은 경우 예외 발생
     */
    private void checkCourseAccessForUser(Long userId, long roadmapId, CompanyInfo companyInfo) {
        if (userId == null) { checkIfCourseIsAllowedForUnauthenticatedUser(roadmapId, companyInfo); }
    }

    private void checkIfCourseIsAllowedForUnauthenticatedUser(long roadmapId, CompanyInfo companyInfo) {
        boolean isAllowedCourse = roadmapRepository.findTop2ByCompanyInfoIdOrderByCourseOrder(companyInfo.getId())
                .stream().anyMatch(roadmap -> roadmap.getId().equals(roadmapId));
        if (!isAllowedCourse) { throw new UnauthorizedCourseAccessException(); }
    }

    private Roadmap getRoadmapWithCourseByCompanyInfo(Long courseId, CompanyInfo companyInfo) {
        return roadmapRepository.findRoadmapWithCourseByCourseIdAndCompanyInfoId(courseId, companyInfo.getId())
                .orElseThrow(RoadmapNotMatchCourseAndCompanyInfoException::new);
    }

    /**
     * 선택한 코스의 상세 목록과 회사 정보가 매칭되는 정보를 가져옵니다.
     * 만약 매칭되는 상세 목록이 하나도 없다면 선택한 코스의 모든 목록을 가져옵니다.
     */
    private List<CourseDetailWithAbilityDto> getFilteredOrAllCourseDetails(CompanyInfo companyInfo, Long courseId) {
        List<Long> filteredCourseDetailIds = courseDetailRepository.getMatchingCourseDetailIdsForCompanyAbility(companyInfo.getId());
        List<CourseDetailWithAbilityDto> courseDetailList = courseDetailRepository.getCourseDetailListWithAbilityByCourseIdOrderByName(courseId, filteredCourseDetailIds);

        List<CourseDetailWithAbilityDto> filteredList = courseDetailList.stream()
                .filter(dto -> dto.getCompanyRequiredAbilityId() != null)
                .collect(Collectors.toList());

        if (filteredList.isEmpty()) {
            return courseDetailList;
        }
        return filteredList;
    }
}
