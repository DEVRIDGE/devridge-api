package io.devridge.api.service.education_materials;

import io.devridge.api.config.auth.LoginUser;
import io.devridge.api.dto.education_materials.CourseVideoWithLikeDto;
import io.devridge.api.dto.education_materials.LikeCourseVideoRequestDto;
import io.devridge.api.handler.ex.NotFoundCourseVideoException;
import io.devridge.api.repository.education_materials.ApiCourseBookRepository;
import io.devridge.api.repository.education_materials.ApiCourseVideoRepository;
import io.devridge.api.repository.education_materials.ApiCourseVideoUserRepository;
import io.devridge.core.domain.education_materials.book.CourseBook;
import io.devridge.core.domain.education_materials.video.CourseVideo;
import io.devridge.core.domain.education_materials.video.CourseVideoUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EducationMaterialService {

    private final ApiCourseVideoRepository courseVideoRepository;
    private final ApiCourseBookRepository courseBookRepository;
    private final ApiCourseVideoUserRepository courseVideoUserRepository;

    @Transactional
    public boolean clickLikeOnCourseVideo(LikeCourseVideoRequestDto likeCourseVideoRequestDto, LoginUser loginUser) {
        CourseVideo courseVideo = courseVideoRepository.findById(likeCourseVideoRequestDto.getCourseVideoId())
                .orElseThrow(NotFoundCourseVideoException::new);

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

    public List<CourseVideoWithLikeDto> getCourseVideos(Long courseDetailId, Long loginUserId) {

        return courseVideoRepository.findWithLikeCntByCourseDetailIdOrderByLikeCntDesc(courseDetailId, loginUserId);
    }

    public List<CourseBook> getCourseBooks(Long courseDetailId) {

        return courseBookRepository.findByCourseDetailIdOrderByLanguage(courseDetailId);
    }
}
