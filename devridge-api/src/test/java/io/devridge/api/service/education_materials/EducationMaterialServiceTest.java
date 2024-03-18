package io.devridge.api.service.education_materials;

import io.devridge.api.config.auth.LoginUser;
import io.devridge.api.dto.education_materials.CourseVideoWithLikeDto;
import io.devridge.api.dto.education_materials.LikeCourseVideoRequestDto;
import io.devridge.api.handler.ex.NotFoundCourseVideoException;
import io.devridge.api.repository.education_materials.ApiCourseBookRepository;
import io.devridge.api.repository.education_materials.ApiCourseVideoRepository;
import io.devridge.api.repository.education_materials.ApiCourseVideoUserRepository;
import io.devridge.core.domain.education_materials.book.BookLanguage;
import io.devridge.core.domain.education_materials.book.CourseBook;
import io.devridge.core.domain.education_materials.video.CourseVideo;
import io.devridge.core.domain.education_materials.video.CourseVideoUser;
import io.devridge.core.domain.education_materials.video.VideoLanguage;
import io.devridge.core.domain.education_materials.video.VideoSource;
import io.devridge.core.domain.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EducationMaterialServiceTest {

    @InjectMocks
    private EducationMaterialService educationMaterialService;

    @Mock
    private ApiCourseVideoRepository courseVideoRepository;

    @Mock
    private ApiCourseBookRepository courseBookRepository;

    @Mock
    private ApiCourseVideoUserRepository courseVideoUserRepository;

    @DisplayName("좋아요를 누르지 않은 영상에 좋아요를 누르면 좋아요가 추가되고 true를 반환합니다.")
    @Test
    void clickLikeOnCourseVideo_success_test() {
        // given
        LikeCourseVideoRequestDto likeCourseVideoRequestDto = new LikeCourseVideoRequestDto(1L);
        LoginUser loginUser = LoginUser.builder().user(User.builder().id(1L).build()).build();

        // stub
        CourseVideo courseVideo = CourseVideo.builder().id(1L).owner("소유자").url("testurl")
                .title("자바 배우기").thumbnail("썸네일.png").source(VideoSource.YOUTUBE).language(VideoLanguage.KOR).build();
        CourseVideoUser courseVideoUser = CourseVideoUser.builder().id(1L).courseVideo(courseVideo).user(loginUser.getUser()).build();

        when(courseVideoRepository.findById(anyLong())).thenReturn(Optional.of(courseVideo));
        when(courseVideoUserRepository.findByCourseVideoIdAndUserId(anyLong(), anyLong())).thenReturn(Optional.empty());
        when(courseVideoUserRepository.save(any(CourseVideoUser.class))).thenReturn(courseVideoUser);

        // when
        boolean result = educationMaterialService.clickLikeOnCourseVideo(likeCourseVideoRequestDto, loginUser);

        // then
        assertThat(result).isTrue();
    }

    @DisplayName("좋아요를 누른 영상에 좋아요를 누르면 좋아요가 삭제되고 false를 반환합니다.")
    @Test
    void clickLikeOnCourseVideo_delete_success_test() {
        // given
        LikeCourseVideoRequestDto likeCourseVideoRequestDto = new LikeCourseVideoRequestDto(1L);
        LoginUser loginUser = new LoginUser(User.builder().id(1L).build(), null);

        // stub
        CourseVideo courseVideo = CourseVideo.builder().id(1L).owner("소유자").url("testurl")
                .title("자바 배우기").thumbnail("썸네일.png").source(VideoSource.YOUTUBE).language(VideoLanguage.KOR).build();
        CourseVideoUser courseVideoUser = CourseVideoUser.builder().id(1L).courseVideo(courseVideo).user(loginUser.getUser()).build();

        when(courseVideoRepository.findById(anyLong())).thenReturn(Optional.of(courseVideo));
        when(courseVideoUserRepository.findByCourseVideoIdAndUserId(anyLong(), anyLong())).thenReturn(Optional.of(courseVideoUser));
        doNothing().when(courseVideoUserRepository).delete(courseVideoUser);

        // when
        boolean result = educationMaterialService.clickLikeOnCourseVideo(likeCourseVideoRequestDto, loginUser);

        // then
        assertThat(result).isFalse();
    }

    @DisplayName("비디오가 없다면 예외를 던집니다.")
    @Test
    void clickLikeOnCourseVideo_fail_test() {
        // given
        LikeCourseVideoRequestDto likeCourseVideoRequestDto = new LikeCourseVideoRequestDto(1L);
        LoginUser loginUser = new LoginUser(User.builder().id(1L).build(), null);

        // stub
        when(courseVideoRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> educationMaterialService.clickLikeOnCourseVideo(likeCourseVideoRequestDto, loginUser))
                .isInstanceOf(NotFoundCourseVideoException.class);
    }

    @DisplayName("영상 리스트와 좋아요 수를 조회합니다.")
    @Test
    void getCourseVideos_success_test() {
        // given
        long loginUserId = 1L;
        long courseDetailId = 1L;

        // stub
        CourseVideoWithLikeDto courseVideoWithLikeDto1 = CourseVideoWithLikeDto.builder().id(1L).title("자바 배우기").thumbnail("썸네일.png").url("testurl").source(VideoSource.YOUTUBE).courseVideoUserId(1L).likeCnt(10L).build();
        CourseVideoWithLikeDto courseVideoWithLikeDto2 = CourseVideoWithLikeDto.builder().id(2L).title("Effective Java").thumbnail("썸네일.png").url("testurl").source(VideoSource.YOUTUBE).courseVideoUserId(null).likeCnt(5L).build();

        when(courseVideoRepository.findWithLikeCntByCourseDetailIdOrderByLikeCntDesc(anyLong(), anyLong())).thenReturn(List.of(courseVideoWithLikeDto1, courseVideoWithLikeDto2));

        // when
        List<CourseVideoWithLikeDto> result = educationMaterialService.getCourseVideos(loginUserId, courseDetailId);

        // then
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0).getId()).isEqualTo(1L);
        assertThat(result.get(0).getTitle()).isEqualTo("자바 배우기");
        assertThat(result.get(0).getSource()).isEqualTo(VideoSource.YOUTUBE);
        assertThat(result.get(0).getUserLikedYn()).isEqualTo("YES");
        assertThat(result.get(0).getLikeCnt()).isEqualTo(10L);
        assertThat(result.get(1).getId()).isEqualTo(2L);
        assertThat(result.get(1).getTitle()).isEqualTo("Effective Java");
        assertThat(result.get(1).getSource()).isEqualTo(VideoSource.YOUTUBE);
        assertThat(result.get(1).getUserLikedYn()).isEqualTo("NO");
        assertThat(result.get(1).getLikeCnt()).isEqualTo(5L);
    }

    @DisplayName("영상 리스트가 없다면 빈 리스트를 반환합니다.")
    @Test
    void getCourseVideos_empty_test() {
        // given
        long loginUserId = 1L;
        long courseDetailId = 1L;

        // stub
        when(courseVideoRepository.findWithLikeCntByCourseDetailIdOrderByLikeCntDesc(anyLong(), anyLong())).thenReturn(List.of());

        // when
        List<CourseVideoWithLikeDto> result = educationMaterialService.getCourseVideos(loginUserId, courseDetailId);

        // then
        assertThat(result.size()).isEqualTo(0);
    }

    @DisplayName("책 리스트를 조회합니다.")
    @Test
    void getCourseBooks_success_test() {
        // given
        long courseDetailId = 1L;

        // stub
        CourseBook courseBook1 = CourseBook.builder().id(1L).title("자바의 정석").language(BookLanguage.KOR).url("test url").build();
        CourseBook courseBook2 = CourseBook.builder().id(2L).title("Effective Java").language(BookLanguage.ENG).url("test url").build();

        when(courseBookRepository.findByCourseDetailIdOrderByLanguage(anyLong())).thenReturn(List.of(courseBook1, courseBook2));

        // when
        List<CourseBook> result = educationMaterialService.getCourseBooks(courseDetailId);

        // then
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0).getId()).isEqualTo(1L);
        assertThat(result.get(0).getTitle()).isEqualTo("자바의 정석");
        assertThat(result.get(0).getLanguage()).isEqualTo(BookLanguage.KOR);
        assertThat(result.get(1).getId()).isEqualTo(2L);
        assertThat(result.get(1).getTitle()).isEqualTo("Effective Java");
        assertThat(result.get(1).getLanguage()).isEqualTo(BookLanguage.ENG);
    }

    @DisplayName("책 리스트가 없다면 빈 리스트를 반환합니다.")
    @Test
    void getCourseBooks_empty_test() {
        // given
        long courseDetailId = 1L;

        // stub
        when(courseBookRepository.findByCourseDetailIdOrderByLanguage(anyLong())).thenReturn(List.of());

        // when
        List<CourseBook> result = educationMaterialService.getCourseBooks(courseDetailId);

        // then
        assertThat(result.size()).isEqualTo(0);
    }
}
