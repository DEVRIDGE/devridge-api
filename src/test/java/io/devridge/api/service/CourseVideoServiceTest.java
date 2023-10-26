package io.devridge.api.service;

import io.devridge.api.config.auth.LoginUser;
import io.devridge.api.domain.companyinfo.*;
import io.devridge.api.domain.roadmap.*;
import io.devridge.api.domain.user.User;
import io.devridge.api.domain.video.CourseVideo;
import io.devridge.api.domain.video.CourseVideoRepository;
import io.devridge.api.dto.CourseVideoResponseDto;
import io.devridge.api.handler.ex.CompanyInfoNotFoundException;
import io.devridge.api.handler.ex.CourseDetailNotFoundException;
import io.devridge.api.handler.ex.UnauthorizedCourseAccessException;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CourseVideoServiceTest {
    @InjectMocks
    private CourseVideoService courseVideoService;
    @Mock
    private CourseToDetailRepository courseToDetailRepository;
    @Mock
    private CourseVideoRepository courseVideoRepository;
    @Mock
    private CompanyInfoRepository companyInfoRepository;
    @Mock
    private RoadmapRepository roadmapRepository;

    @DisplayName("코스 상세에 해당하는 영상 목록을 성공적으로 가져온다.")
    @Test
    public void get_course_video_list_success() {
        //given
        LoginUser loginUser = LoginUser.builder().user(User.builder().id(1L).build()).build();
        CompanyInfo companyInfo = makeCompanyInfo();
        Course course = Course.builder().id(1L).name("언어").build();
        CourseDetail courseDetail = CourseDetail.builder().id(1L).name("Java").build();
        CourseToDetail courseToDetail = CourseToDetail.builder().course(course).courseDetail(courseDetail).build();
        List<CourseVideo> courseVideoList = makeCourseVideoList(courseDetail);

        //stub
        when(courseToDetailRepository.findFetchByCourseIdAndCourseDetailId(anyLong(), anyLong())).thenReturn(Optional.of(courseToDetail));
        when(companyInfoRepository.findByCompanyIdAndJobIdAndDetailedPositionId(anyLong(), anyLong(), anyLong())).thenReturn(Optional.of(companyInfo));
        when(courseVideoRepository.findByCourseDetailIdOrderByLikeCntDesc(anyLong())).thenReturn(courseVideoList);

        //when
        CourseVideoResponseDto courseVideoResponseDto = courseVideoService.getCourseVideoList(1L, 1L, 1L, 1L, 1L, loginUser);

        //then
        assertThat(courseVideoResponseDto.getCourseVideos().size()).isEqualTo(2);
        assertThat(courseVideoResponseDto.getCourseVideos().get(0).getTitle()).isEqualTo("Java 강의 영상1");
        assertThat(courseVideoResponseDto.getCourseVideos().get(1).getTitle()).isEqualTo("Java 강의 영상2");
        assertThat(courseVideoResponseDto.getCourseTitle()).isEqualTo("언어");
        assertThat(courseVideoResponseDto.getCourseDetailTitle()).isEqualTo("Java");
    }

    @DisplayName("로그인 안한 유저가 허용된 영상 목록 요청시 성공적으로 가져온다.")
    @Test
    public void get_allowed_course_video_list_with_not_login_user() {
        //given
        CompanyInfo companyInfo = makeCompanyInfo();
        Course course = Course.builder().id(1L).name("언어").build();
        CourseDetail courseDetail = CourseDetail.builder().id(1L).name("Java").build();
        CourseToDetail courseToDetail = CourseToDetail.builder().course(course).courseDetail(courseDetail).build();
        List<Roadmap> roadmapList = new ArrayList<>();
        roadmapList.add(Roadmap.builder().id(1L).course(course).build());
        List<CourseVideo> courseVideoList = makeCourseVideoList(courseDetail);

        //stub
        when(courseToDetailRepository.findFetchByCourseIdAndCourseDetailId(anyLong(), anyLong())).thenReturn(Optional.of(courseToDetail));
        when(companyInfoRepository.findByCompanyIdAndJobIdAndDetailedPositionId(anyLong(), anyLong(), anyLong())).thenReturn(Optional.of(companyInfo));
        when(roadmapRepository.findTop2ByCompanyInfoIdOrderByCourseOrder(anyLong())).thenReturn(roadmapList);
        when(courseVideoRepository.findByCourseDetailIdOrderByLikeCntDesc(anyLong())).thenReturn(courseVideoList);

        //when
        CourseVideoResponseDto courseVideoResponseDto = courseVideoService.getCourseVideoList(1L, 1L, 1L, 1L, 1L, null);

        //then
        assertThat(courseVideoResponseDto.getCourseTitle()).isEqualTo("언어");
        assertThat(courseVideoResponseDto.getCourseDetailTitle()).isEqualTo("Java");
        assertThat(courseVideoResponseDto.getCourseVideos().size()).isEqualTo(2);
        assertThat(courseVideoResponseDto.getCourseVideos().get(0).getTitle()).isEqualTo("Java 강의 영상1");
        assertThat(courseVideoResponseDto.getCourseVideos().get(1).getTitle()).isEqualTo("Java 강의 영상2");
    }

    @DisplayName("로그인 안한 유저가 허용되지 않은 영상 목록 요청시 예외를 발생시킨다.")
    @Test
    public void get_not_allowed_course_video_list_with_not_login_user_throw_exception() {
        //given
        CompanyInfo companyInfo = makeCompanyInfo();
        Course course1 = Course.builder().id(1L).name("언어").build();
        Course course3 = Course.builder().id(3L).name("데이터베이스").build();
        CourseDetail courseDetail = CourseDetail.builder().id(1L).name("Mysql").build();
        CourseToDetail courseToDetail = CourseToDetail.builder().course(course3).courseDetail(courseDetail).build();
        List<Roadmap> roadmapList = new ArrayList<>();
        roadmapList.add(Roadmap.builder().id(1L).course(course1).build());

        //stub
        when(courseToDetailRepository.findFetchByCourseIdAndCourseDetailId(anyLong(), anyLong())).thenReturn(Optional.of(courseToDetail));
        when(companyInfoRepository.findByCompanyIdAndJobIdAndDetailedPositionId(anyLong(), anyLong(), anyLong())).thenReturn(Optional.of(companyInfo));
        when(roadmapRepository.findTop2ByCompanyInfoIdOrderByCourseOrder(anyLong())).thenReturn(roadmapList);

        //when & then
        assertThatThrownBy(() -> courseVideoService.getCourseVideoList(3L, 1L, 1L, 1L, 1L, null))
                .isInstanceOf(UnauthorizedCourseAccessException.class);
    }

    @DisplayName("코스 영상 목록을 가져올 때 맞는 코스 상세 정보를 찾을 수 없으면 예외를 발생시킨다.")
    @Test
    void get_course_video_list_if_not_found_course_detail_throw_exception() {
        //given
        LoginUser loginUser = LoginUser.builder().user(User.builder().id(1L).build()).build();

        //stub
        when(courseToDetailRepository.findFetchByCourseIdAndCourseDetailId(anyLong(), anyLong())).thenReturn(Optional.empty());

        //when & then
        assertThatThrownBy(() -> courseVideoService.getCourseVideoList(1L, 1L, 1L, 1L, 1L, loginUser))
                .isInstanceOf(CourseDetailNotFoundException.class);
    }

    @DisplayName("영상 목록을 가져올 때 맞는 회사 정보를 찾을 수 없으면 예외를 발생시킨다.")
    @Test
    void get_course_video_list_if_not_found_company_info_throw_exception() {
        //given
        LoginUser loginUser = LoginUser.builder().user(User.builder().id(1L).build()).build();
        Course course = Course.builder().id(1L).name("언어").build();
        CourseDetail courseDetail = CourseDetail.builder().id(1L).name("Java").build();
        CourseToDetail courseToDetail = CourseToDetail.builder().course(course).courseDetail(courseDetail).build();

        //stub
        when(courseToDetailRepository.findFetchByCourseIdAndCourseDetailId(anyLong(), anyLong())).thenReturn(Optional.of(courseToDetail));
        when(companyInfoRepository.findByCompanyIdAndJobIdAndDetailedPositionId(anyLong(), anyLong(), anyLong())).thenReturn(Optional.empty());

        //when & then
        assertThatThrownBy(() -> courseVideoService.getCourseVideoList(1L, 1L, 1L, 1L, 1L, loginUser))
                .isInstanceOf(CompanyInfoNotFoundException.class);
    }

    private List<CourseVideo> makeCourseVideoList(CourseDetail courseDetail) {
        List<CourseVideo> courseVideoList = new ArrayList<>();
        courseVideoList.add(CourseVideo.builder().id(1L).title("Java 강의 영상1").courseDetail(courseDetail).build());
        courseVideoList.add(CourseVideo.builder().id(2L).title("Java 강의 영상2").courseDetail(courseDetail).build());
        return courseVideoList;
    }

    private CompanyInfo makeCompanyInfo() {
        Company company = Company.builder().id(1L).name("test company").build();
        Job job = Job.builder().id(1L).name("test job").build();
        DetailedPosition detailedPosition = DetailedPosition.builder().id(1L).name("test detailed position").build();
        return CompanyInfo.builder().id(1L).company(company).job(job).detailedPosition(detailedPosition).build();
    }
}
