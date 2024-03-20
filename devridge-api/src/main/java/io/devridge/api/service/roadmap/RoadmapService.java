package io.devridge.api.service.roadmap;

import io.devridge.api.config.auth.LoginUser;

import io.devridge.api.dto.roadmap.*;
import io.devridge.api.dto.education_materials.CourseVideoWithLikeDto;
import io.devridge.api.handler.ex.*;
import io.devridge.api.handler.ex.recruitment.RecruitmentInfoNotFoundException;
import io.devridge.api.repository.roadmap.ApiCourseDetailRepository;
import io.devridge.api.repository.roadmap.ApiCourseToDetailRepository;
import io.devridge.api.repository.roadmap.ApiRoadmapRepository;
import io.devridge.api.repository.roadmap.ApiUserRoadmapRepository;
import io.devridge.api.service.education_materials.EducationMaterialService;
import io.devridge.api.service.recruitment.RecruitmentInfoService;
import io.devridge.core.domain.education_materials.book.CourseBook;
import io.devridge.core.domain.recruitment.RecruitmentInfo;
import io.devridge.core.domain.roadmap.*;
import io.devridge.core.domain.user.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class RoadmapService {

    private final RecruitmentInfoService recruitmentInfoService;
    private final EducationMaterialService educationMaterialService;

    private final ApiCourseDetailRepository courseDetailRepository;
    private final ApiCourseToDetailRepository courseToDetailRepository;
    private final ApiRoadmapRepository roadmapRepository;
    private final ApiUserRoadmapRepository userRoadmapRepository;
    private final CourseRepository courseRepository;

    @Transactional(readOnly = true)
    public RoadmapDto getRoadmap(Long jobId, Long detailedPositionId, LoginUser loginUser) {
        RecruitmentInfo recruitmentInfo = findRecruitmentInfo(jobId, detailedPositionId);
        List<CourseIndexList> roadmap = getRoadmap(recruitmentInfo, getLoginUserId(loginUser));

        return new RoadmapDto(recruitmentInfo, roadmap);
    }

    @Transactional(readOnly = true)
    public CourseDetailResponseDto getCourseDetails(Long courseId, Long jobId, Long detailedPositionId, LoginUser loginUser) {
        RecruitmentInfo recruitmentInfo = findRecruitmentInfo(jobId, detailedPositionId);

        Roadmap roadmap = getRoadmapWithCourseByRecruitmentInfo(courseId, recruitmentInfo);

        Long loginUserId = getLoginUserId(loginUser);

        checkCourseAuthorization(loginUserId, roadmap.getId(), recruitmentInfo, (r, cid) -> r.getId().equals(cid));

        List<CourseDetail> courseDetailList = getFilteredOrAllCourseDetails(recruitmentInfo, courseId);

        return new CourseDetailResponseDto(roadmap.getCourse().getName(), getUserStudyStatus(loginUser, roadmap), courseDetailList);
    }

    @Transactional(readOnly = true)
    public CourseItemResponseDto getCourseVideoAndBookList(Long courseId, Long courseDetailId, Long jobId, Long detailedPositionId, LoginUser loginUser) {
        CourseToDetail courseToDetail = findCourseInfo(courseId, courseDetailId);

        RecruitmentInfo recruitmentInfo = findRecruitmentInfo(jobId, detailedPositionId);

        Long loginUserId = getLoginUserId(loginUser);

        checkCourseAuthorization(loginUserId, courseId, recruitmentInfo, (r, cid) -> r.getCourse().getId().equals(cid));

        List<CourseVideoWithLikeDto> courseVideoList = getCourseVideos(courseDetailId, loginUserId);

        List<CourseBook> courseBookList = getCourseBooks(courseDetailId);

        return new CourseItemResponseDto(courseToDetail, courseVideoList, courseBookList);
    }

    // TODO: roadmapId를 받아서 간단하게 변경하는 방식으로 변경하는게 좋을 것 같음
    @Transactional
    public void changeStudyStatus(Long courseId, ChangeStudyStatusRequestDto changeStudyStatusRequestDto, LoginUser loginUser) {
        RecruitmentInfo recruitmentInfo = recruitmentInfoService.validateCompanyInfo(changeStudyStatusRequestDto.getCompanyId(), changeStudyStatusRequestDto.getJobId(), changeStudyStatusRequestDto.getDetailedPositionId());
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new CourseNotFoundException("해당 코스 정보를 찾을 수 없습니다."));
        Roadmap roadmap = roadmapRepository.findByCourseIdAndRecruitmentInfoId(course.getId(), recruitmentInfo.getId()).orElseThrow(RoadmapNotMatchCourseAndCompanyInfoException::new);

        if(changeStudyStatusRequestDto.getStudyStatus().equals(StudyStatus.BEFORE_STUDYING)) {
            Optional<UserRoadmap> userRoadmap = userRoadmapRepository.findByUserIdAndRoadmapId(loginUser.getUser().getId(), roadmap.getId());
            userRoadmap.ifPresent(userRoadmapRepository::delete);
        }
        else {
            Optional<UserRoadmap> userRoadmap = userRoadmapRepository.findByUserIdAndRoadmapId(loginUser.getUser().getId(), roadmap.getId());
            StudyStatus targetStudyStatus = changeStudyStatusRequestDto.getStudyStatus().equals(StudyStatus.STUDYING) ? StudyStatus.STUDYING : StudyStatus.STUDY_END;
            if(userRoadmap.isEmpty()) {
                userRoadmapRepository.save(UserRoadmap.builder()
                        .user(loginUser.getUser())
                        .roadmap(roadmap)
                        .studyStatus(targetStudyStatus)
                        .build());
            }
            else {
                userRoadmap.get().changeStudyStatus(targetStudyStatus);
            }
        }
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

    private RecruitmentInfo findRecruitmentInfo(Long jobId, Long detailedPositionId) {
        return recruitmentInfoService.findRecruitmentInfo(jobId, detailedPositionId)
                .orElseThrow(RecruitmentInfoNotFoundException::new);
    }

    private List<CourseIndexList> getRoadmap(RecruitmentInfo recruitmentInfo, Long userId) {
        List<RoadmapAndStatusDto> roadmapAndStatusDtoList = getRoadmapAndStatusByRecruitmentAndUser(recruitmentInfo.getId(), userId);
        Collection<List<CourseInfoDto>> groupedRoadmapDto = convertDtoAndGroupRoadmapByOrder(roadmapAndStatusDtoList);
        return addEmptyListAndIndex(groupedRoadmapDto);
    }

    private List<RoadmapAndStatusDto> getRoadmapAndStatusByRecruitmentAndUser(Long recruitmentInfoId, Long userId) {
        return roadmapRepository.getRoadmapListByRecruitmentInfoIdAndUserId(recruitmentInfoId, userId);
    }

    private Collection<List<CourseInfoDto>> convertDtoAndGroupRoadmapByOrder(List<RoadmapAndStatusDto> roadmapAndStatusDtoList) {
        return roadmapAndStatusDtoList.stream()
                .map(CourseInfoDto::new)
                .collect(Collectors.groupingBy(CourseInfoDto::getOrder, TreeMap::new, Collectors.toList()))
                .values();
    }

    /**
     * 프론트 요구사항
     * 1. 컬렉션의 첫 번째 항목이 SKILL인 경우 빈 리스트를 추가
     * 2. 컬렉션의 이전 항목과 현재 항목이 모두 SKILL인 경우 빈 리스트 추가
     * 3. 인덱스 추가
     */
    private List<CourseIndexList> addEmptyListAndIndex(Collection<List<CourseInfoDto>> courseListCollection) {
        List<CourseIndexList> courseIndexList = new ArrayList<>();
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
                    courseIndexList.add(new CourseIndexList(index++, Collections.emptyList()));
                }
            }
            // 2. 이전 항목이 SKILL이고 현재 항목이 SKILL인 경우 빈 배열을 추가
            if (previousCourseType == CourseType.SKILL && currentCourseType == CourseType.SKILL) {
                courseIndexList.add(new CourseIndexList(index++, Collections.emptyList()));
            }
            // 원래 리스트 항목 추가, 인덱스 추가
            courseIndexList.add(new CourseIndexList(index++, courseInfoList));
            previousCourseType = currentCourseType;
        }
        return courseIndexList;
    }

    /**
     * 로그인을 하지 않은 경우 접근이 허가된 코스인지 체크하고 허가되지 않은 경우 예외 발생
     */
    private void checkCourseAuthorization(Long userId, Long courseId, RecruitmentInfo recruitmentInfo, BiPredicate<Roadmap, Long> isMatch) {
        if (userId == null) {
            checkIfCourseIsAllowedForUnauthenticatedUser(courseId, recruitmentInfo, isMatch);
        }
    }

    private void checkIfCourseIsAllowedForUnauthenticatedUser(Long courseId, RecruitmentInfo recruitmentInfo, BiPredicate<Roadmap, Long> isMatch) {
        boolean isAllowedCourse = roadmapRepository.findTop2ByRecruitmentInfoIdOrderByCourseOrder(recruitmentInfo.getId())
                .stream().anyMatch(roadmap -> isMatch.test(roadmap, courseId));
        if (!isAllowedCourse) { throw new UnAuthorizedCourseAccessException(); }
    }

    // TODO : 예외 - CompanyInfo가 아니라 RecruitmentInfo로 변경
    private Roadmap getRoadmapWithCourseByRecruitmentInfo(long courseId, RecruitmentInfo recruitmentInfo) {
        return roadmapRepository.findRoadmapWithCourseByCourseIdAndRecruitmentInfoId(courseId, recruitmentInfo.getId())
                .orElseThrow(RoadmapNotMatchCourseAndCompanyInfoException::new);
    }

    /**
     * 회사에서 요구하는 스킬과 매칭되는 CourseDetail 리스트를 응답 합니다.
     * 만약 매칭되는 CourseDetail이 없다면 모든 CourseDetail 리스트를 응답 합니다.
     */
    private List<CourseDetail> getFilteredOrAllCourseDetails(RecruitmentInfo recruitmentInfo, long courseId) {
        List<CourseDetail> courseDetailList = courseDetailRepository.getCourseDetailListWithAbilityByCourseIdOrderByName(courseId, recruitmentInfo.getId());

        if(!courseDetailList.isEmpty()) {
            return courseDetailList;
        }
        return courseDetailRepository.getAllCourseDetailByCourseIdOrderByName(courseId);
    }

    private CourseToDetail findCourseInfo(long courseId, long courseDetailId) {
        return courseToDetailRepository.findFetchByCourseIdAndCourseDetailId(courseId, courseDetailId)
                .orElseThrow(() -> new CourseDetailNotFoundException("해당하는 세부코스가 없습니다."));
    }

    private List<CourseVideoWithLikeDto> getCourseVideos(Long courseDetailId, Long loginUserId) {
        return educationMaterialService.getCourseVideos(courseDetailId, loginUserId);
    }

    private List<CourseBook> getCourseBooks(long courseDetailId) {
        return educationMaterialService.getCourseBooks(courseDetailId);
    }
}
