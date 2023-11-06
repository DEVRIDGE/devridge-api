package io.devridge.api.service;

import io.devridge.api.config.auth.LoginUser;
import io.devridge.api.domain.book.CourseBook;
import io.devridge.api.domain.book.CourseBookRepository;
import io.devridge.api.domain.companyinfo.CompanyInfo;
import io.devridge.api.domain.companyinfo.CompanyInfoRepository;
import io.devridge.api.domain.roadmap.*;
import io.devridge.api.domain.video.CourseVideo;
import io.devridge.api.domain.video.CourseVideoRepository;
import io.devridge.api.domain.video.CourseVideoUser;
import io.devridge.api.domain.video.CourseVideoUserRepository;
import io.devridge.api.dto.item.CourseItemResponseDto;
import io.devridge.api.dto.item.CourseVideoWithLikeDto;
import io.devridge.api.dto.item.LikeCourseVideoRequestDto;
import io.devridge.api.handler.ex.CompanyInfoNotFoundException;
import io.devridge.api.handler.ex.CourseDetailNotFoundException;
import io.devridge.api.handler.ex.NotFoundCourseVideoException;
import io.devridge.api.handler.ex.UnauthorizedCourseAccessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CourseItemService {
    private final CourseVideoRepository courseVideoRepository;
    private final CompanyInfoRepository companyInfoRepository;
    private final RoadmapRepository roadmapRepository;
    private final CourseToDetailRepository courseToDetailRepository;
    private final CourseVideoUserRepository courseVideoUserRepository;
    private final CourseBookRepository courseBookRepository;

    /**
     * 코스 ID 추가 - 코스와 코스 디테일 n:n 변화로 인한 파라미터 추가
     */
    @Transactional(readOnly = true)
    public CourseItemResponseDto getCourseVideoAndBookList(long courseId, long courseDetailId, long companyId, long jobId, long detailedPositionId, LoginUser loginUser) {
        CourseToDetail courseToDetail = courseToDetailRepository.findFetchByCourseIdAndCourseDetailId(courseId, courseDetailId)
                .orElseThrow(() -> new CourseDetailNotFoundException("해당하는 세부코스가 없습니다."));

        CompanyInfo companyInfo = findCompanyInfo(companyId, jobId, detailedPositionId);
        checkCourseAccessForUser(getLoginUserId(loginUser), courseId, companyInfo);

        Long userId = loginUser == null ? null : loginUser.getUser().getId();

        List<CourseVideoWithLikeDto> courseVideoList = courseVideoRepository.findWithLikeCntByCourseDetailIdOrderByLikeCntDesc(userId, courseDetailId);

        List<CourseBook> courseBookList = courseBookRepository.findByCourseDetailIdOrderByLanguage(courseDetailId);

        return new CourseItemResponseDto(courseToDetail.getCourse().getName(), courseToDetail.getCourseDetail().getName(), courseToDetail.getCourseDetail().getDescription(), courseVideoList, courseBookList);
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

    @Transactional
    public boolean clickLikeOnCourseVideo(LikeCourseVideoRequestDto likeCourseVideoRequestDto, LoginUser loginUser) {
        CourseVideo courseVideo = courseVideoRepository.findById(likeCourseVideoRequestDto.getCourseVideoId()).orElseThrow(() -> new NotFoundCourseVideoException());

        Optional<CourseVideoUser> courseVideoUser = courseVideoUserRepository.findByCourseVideoIdAndUserId(likeCourseVideoRequestDto.getCourseVideoId(), loginUser.getUser().getId());

        if(courseVideoUser.isEmpty()) {
            CourseVideoUser newCourseVideoUser = CourseVideoUser.builder()
                    .courseVideo(courseVideo)
                    .user(loginUser.getUser())
                    .build();
            courseVideoUserRepository.save(newCourseVideoUser);
            return true; // 좋아요 안했음 -> 좋아요를 하면 true를 반환
        }
        else {
            courseVideoUserRepository.delete(courseVideoUser.get());
            return false; // 좋아요 -> 좋아요 해제를 하면 false를 반환
        }
    }
}
