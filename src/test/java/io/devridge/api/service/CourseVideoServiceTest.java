package io.devridge.api.service;

import io.devridge.api.domain.roadmap.Course;
import io.devridge.api.domain.roadmap.CourseDetail;
import io.devridge.api.domain.roadmap.CourseDetailRepository;
import io.devridge.api.domain.video.CourseVideo;
import io.devridge.api.domain.video.CourseVideoRepository;
import io.devridge.api.dto.CourseVideoResponseDto;
import io.devridge.api.handler.ex.CourseDetailNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CourseVideoServiceTest {

    @InjectMocks
    private CourseVideoService courseVideoService;

    @Mock
    private CourseDetailRepository courseDetailRepository;

    @Mock
    private CourseVideoRepository courseVideoRepository;

    @DisplayName("특정 CourseDetail의 영상 리스트를 정상적으로 조회한다.")
    @Test
    public void getCourseVideoList_success_test() {
        //given
        long courseDetailId = 1L;
        Course course = Course.builder().id(courseDetailId).name("언어").build();
        CourseDetail courseDetail = CourseDetail.builder().id(1L).name("Java").course(course).build();

        List<CourseVideo> courseVideoList = makeCourseVideoList(courseDetail);

        //stub
        when(courseDetailRepository.findById(courseDetailId)).thenReturn(Optional.of(courseDetail));
        when(courseVideoRepository.findByCourseDetailIdOrderByLikeCntDesc(courseDetailId)).thenReturn(courseVideoList);

        //when
        CourseVideoResponseDto courseVideoResponseDto = courseVideoService.getCourseVideoList(courseDetailId);

        //then
        assertThat(courseVideoResponseDto.getCourseVideos().size()).isEqualTo(2);
        assertThat(courseVideoResponseDto.getCourseVideos().get(0).getTitle()).isEqualTo("Java 강의 영상1");
        assertThat(courseVideoResponseDto.getCourseVideos().get(1).getTitle()).isEqualTo("Java 강의 영상2");
        assertThat(courseVideoResponseDto.getCourseTitle()).isEqualTo("언어");
        assertThat(courseVideoResponseDto.getCourseDetailTitle()).isEqualTo("Java");
    }

    @DisplayName("영상 조회시, 대응되는 CourseDetail이 없으면 에러를 던진다")
    @Test
    public void getCourseVideoList_fail_test() {
        //given
        long courseDetailId = 100L;


        //stub
        when(courseDetailRepository.findById(courseDetailId)).thenReturn(Optional.empty());

        //when & then
        assertThatThrownBy(() -> courseVideoService.getCourseVideoList(courseDetailId))
                .isInstanceOf(CourseDetailNotFoundException.class);
    }

    private List<CourseVideo> makeCourseVideoList(CourseDetail courseDetail) {
        List<CourseVideo> courseVideoList = new ArrayList<>();
        courseVideoList.add(CourseVideo.builder().id(1L).title("Java 강의 영상1").courseDetail(courseDetail).build());
        courseVideoList.add(CourseVideo.builder().id(2L).title("Java 강의 영상2").courseDetail(courseDetail).build());
        return courseVideoList;
    }
}
