package io.devridge.api.service;

import io.devridge.api.config.auth.LoginUser;
import io.devridge.api.domain.companyinfo.CompanyInfo;
import io.devridge.api.domain.companyinfo.CompanyInfoRepository;
import io.devridge.api.domain.roadmap.CourseDetail;
import io.devridge.api.domain.roadmap.CourseDetailRepository;
import io.devridge.api.domain.roadmap.RoadmapRepository;
import io.devridge.api.domain.video.CourseVideo;
import io.devridge.api.domain.video.CourseVideoRepository;
import io.devridge.api.dto.CourseVideoResponseDto;
import io.devridge.api.handler.ex.CompanyInfoNotFoundException;
import io.devridge.api.handler.ex.CourseDetailNotFoundException;
import io.devridge.api.handler.ex.UnauthorizedCourseAccessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseVideoService {

    private final CourseDetailRepository courseDetailRepository;
    private final CourseVideoRepository courseVideoRepository;
    private final CompanyInfoRepository companyInfoRepository;
    private final RoadmapRepository roadmapRepository;

    @Transactional(readOnly = true)
    public CourseVideoResponseDto getCourseVideoList(long courseDetailId, long companyId, long jobId, long detailedPositionId, LoginUser loginUser) {
        CourseDetail courseDetail = courseDetailRepository.findById(courseDetailId).orElseThrow(() -> new CourseDetailNotFoundException("해당하는 세부코스가 없습니다."));
        CompanyInfo companyInfo = findCompanyInfo(companyId, jobId, detailedPositionId);

        checkCourseAccessForUser(getLoginUserId(loginUser), courseDetail.getCourse().getId(), companyInfo);

        List<CourseVideo> courseVideoList = courseVideoRepository.findByCourseDetailIdOrderByLikeCntDesc(courseDetailId);

        return new CourseVideoResponseDto(courseDetail.getCourse().getName(), courseDetail.getName(), courseVideoList);
    }

    private CompanyInfo findCompanyInfo(long companyId, long jobId, long detailedPositionId) {
        return companyInfoRepository.findByCompanyIdAndJobIdAndDetailedPositionId(companyId, jobId, detailedPositionId)
                .orElseThrow(() -> new CompanyInfoNotFoundException("회사, 직무, 서비스에 일치 하는 회사 정보가 없습니다."));
    }

    private Long getLoginUserId(LoginUser loginUser) {
        return (loginUser != null) ? loginUser.getUser().getId() : null;
    }

    private void checkCourseAccessForUser(Long userId, long courseId, CompanyInfo companyInfo) {
        if (userId == null) { checkIfCourseIsAllowedForUnauthenticatedUser(courseId, companyInfo); }
    }

    private void checkIfCourseIsAllowedForUnauthenticatedUser(long courseId, CompanyInfo companyInfo) {
        boolean isAllowedCourse = roadmapRepository.findTop2ByCompanyInfoIdOrderByCourseOrder(companyInfo.getId())
                .stream().anyMatch(roadmap -> roadmap.getCourse().getId().equals(courseId));
        if (!isAllowedCourse) { throw new UnauthorizedCourseAccessException(); }
    }
}
