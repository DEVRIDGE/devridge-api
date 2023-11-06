package io.devridge.api.service;

import io.devridge.api.config.auth.LoginUser;
import io.devridge.api.domain.book.BookSource;
import io.devridge.api.domain.book.CourseBook;
import io.devridge.api.domain.book.CourseBookRepository;
import io.devridge.api.domain.companyinfo.*;
import io.devridge.api.domain.roadmap.*;
import io.devridge.api.domain.user.User;
import io.devridge.api.domain.video.CourseVideoRepository;
import io.devridge.api.dto.item.CourseItemResponseDto;
import io.devridge.api.dto.item.CourseVideoWithLikeDto;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CourseItemServiceTest {
    @InjectMocks
    private CourseItemService courseItemService;
    @Mock
    private CourseToDetailRepository courseToDetailRepository;
    @Mock
    private CourseVideoRepository courseVideoRepository;
    @Mock
    private CompanyInfoRepository companyInfoRepository;
    @Mock
    private RoadmapRepository roadmapRepository;
    @Mock
    private CourseBookRepository courseBookRepository;

    @DisplayName("코스 상세에 해당하는 책과 영상 목록을 성공적으로 가져온다.")
    @Test
    public void get_course_video_list_success() {
        //given
        LoginUser loginUser = LoginUser.builder().user(User.builder().id(1L).build()).build();
        CompanyInfo companyInfo = makeCompanyInfo();
        Course course = Course.builder().id(1L).name("언어").build();
        CourseDetail courseDetail = CourseDetail.builder().id(1L).name("Java").description("Java 강의 설명").build();
        CourseToDetail courseToDetail = CourseToDetail.builder().course(course).courseDetail(courseDetail).build();
        List<CourseVideoWithLikeDto> courseVideoList = makeCourseVideoList();
        List<CourseBook> courseBookList = makeCourseBookList();

        //stub
        when(courseToDetailRepository.findFetchByCourseIdAndCourseDetailId(anyLong(), anyLong())).thenReturn(Optional.of(courseToDetail));
        when(companyInfoRepository.findByCompanyIdAndJobIdAndDetailedPositionId(anyLong(), anyLong(), anyLong())).thenReturn(Optional.of(companyInfo));
        when(courseVideoRepository.findWithLikeCntByCourseDetailIdOrderByLikeCntDesc(anyLong(), anyLong())).thenReturn(courseVideoList);
        when(courseBookRepository.findByCourseDetailIdOrderByLanguage(anyLong())).thenReturn(courseBookList);

        //when
        CourseItemResponseDto courseItemResponseDto = courseItemService.getCourseVideoAndBookList(1L, 1L, 1L, 1L, 1L, loginUser);

        //then
        assertThat(courseItemResponseDto.getCourseTitle()).isEqualTo("언어");
        assertThat(courseItemResponseDto.getCourseDetailTitle()).isEqualTo("Java");
        assertThat(courseItemResponseDto.getCourseDetailDescription()).isEqualTo("Java 강의 설명");
        assertThat(courseItemResponseDto.getCourseVideos().size()).isEqualTo(2);
        assertThat(courseItemResponseDto.getCourseVideos().get(0).getTitle()).isEqualTo("Java 강의 영상2");
        assertThat(courseItemResponseDto.getCourseVideos().get(0).getLikeCnt()).isEqualTo(100L);
        assertThat(courseItemResponseDto.getCourseVideos().get(0).getUserLikedYn()).isEqualTo("YES");
        assertThat(courseItemResponseDto.getCourseVideos().get(1).getTitle()).isEqualTo("Java 강의 영상1");
        assertThat(courseItemResponseDto.getCourseVideos().get(1).getLikeCnt()).isEqualTo(0L);
        assertThat(courseItemResponseDto.getCourseVideos().get(1).getUserLikedYn()).isEqualTo("NO");
        assertThat(courseItemResponseDto.getCourseBooks().size()).isEqualTo(2);
        assertThat(courseItemResponseDto.getCourseBooks().get(0).getTitle()).isEqualTo("책1");
        assertThat(courseItemResponseDto.getCourseBooks().get(0).getUrl()).isEqualTo("https://책1.com");
        assertThat(courseItemResponseDto.getCourseBooks().get(0).getThumbnail()).isEqualTo("https://책1_이미지.png");
        assertThat(courseItemResponseDto.getCourseBooks().get(0).getSource()).isEqualTo(BookSource.COUPANG);
        assertThat(courseItemResponseDto.getCourseBooks().get(1).getTitle()).isEqualTo("책2");
        assertThat(courseItemResponseDto.getCourseBooks().get(1).getUrl()).isEqualTo("https://책2.com");
        assertThat(courseItemResponseDto.getCourseBooks().get(1).getThumbnail()).isEqualTo("https://책2_이미지.png");
        assertThat(courseItemResponseDto.getCourseBooks().get(1).getSource()).isEqualTo(BookSource.COUPANG);
    }

    @DisplayName("로그인 안한 유저가 허용된 영상 목록 요청시 성공적으로 가져온다.")
    @Test
    public void get_allowed_course_video_list_with_not_login_user() {
        //given
        CompanyInfo companyInfo = makeCompanyInfo();
        Course course = Course.builder().id(1L).name("언어").build();
        CourseDetail courseDetail = CourseDetail.builder().id(1L).name("Java").description("Java 강의 설명").build();
        CourseToDetail courseToDetail = CourseToDetail.builder().course(course).courseDetail(courseDetail).build();
        List<Roadmap> roadmapList = new ArrayList<>();
        roadmapList.add(Roadmap.builder().id(1L).course(course).build());
        List<CourseVideoWithLikeDto> courseVideoList = makeCourseVideoList();
        List<CourseBook> courseBookList = makeCourseBookList();

        //stub
        when(courseToDetailRepository.findFetchByCourseIdAndCourseDetailId(anyLong(), anyLong())).thenReturn(Optional.of(courseToDetail));
        when(companyInfoRepository.findByCompanyIdAndJobIdAndDetailedPositionId(anyLong(), anyLong(), anyLong())).thenReturn(Optional.of(companyInfo));
        when(roadmapRepository.findTop2ByCompanyInfoIdOrderByCourseOrder(anyLong())).thenReturn(roadmapList);
        when(courseVideoRepository.findWithLikeCntByCourseDetailIdOrderByLikeCntDesc(any(), anyLong())).thenReturn(courseVideoList);
        when(courseBookRepository.findByCourseDetailIdOrderByLanguage(anyLong())).thenReturn(courseBookList);

        //when
        CourseItemResponseDto courseItemResponseDto = courseItemService.getCourseVideoAndBookList(1L, 1L, 1L, 1L, 1L, null);

        //then
        assertThat(courseItemResponseDto.getCourseTitle()).isEqualTo("언어");
        assertThat(courseItemResponseDto.getCourseDetailTitle()).isEqualTo("Java");
        assertThat(courseItemResponseDto.getCourseDetailDescription()).isEqualTo("Java 강의 설명");
        assertThat(courseItemResponseDto.getCourseVideos().size()).isEqualTo(2);
        assertThat(courseItemResponseDto.getCourseVideos().get(0).getTitle()).isEqualTo("Java 강의 영상2");
        assertThat(courseItemResponseDto.getCourseVideos().get(0).getLikeCnt()).isEqualTo(100L);
        assertThat(courseItemResponseDto.getCourseVideos().get(0).getUserLikedYn()).isEqualTo("YES");
        assertThat(courseItemResponseDto.getCourseVideos().get(1).getTitle()).isEqualTo("Java 강의 영상1");
        assertThat(courseItemResponseDto.getCourseVideos().get(1).getLikeCnt()).isEqualTo(0L);
        assertThat(courseItemResponseDto.getCourseVideos().get(1).getUserLikedYn()).isEqualTo("NO");
        assertThat(courseItemResponseDto.getCourseBooks().size()).isEqualTo(2);
        assertThat(courseItemResponseDto.getCourseBooks().get(0).getTitle()).isEqualTo("책1");
        assertThat(courseItemResponseDto.getCourseBooks().get(0).getUrl()).isEqualTo("https://책1.com");
        assertThat(courseItemResponseDto.getCourseBooks().get(0).getThumbnail()).isEqualTo("https://책1_이미지.png");
        assertThat(courseItemResponseDto.getCourseBooks().get(0).getSource()).isEqualTo(BookSource.COUPANG);
        assertThat(courseItemResponseDto.getCourseBooks().get(1).getTitle()).isEqualTo("책2");
        assertThat(courseItemResponseDto.getCourseBooks().get(1).getUrl()).isEqualTo("https://책2.com");
        assertThat(courseItemResponseDto.getCourseBooks().get(1).getThumbnail()).isEqualTo("https://책2_이미지.png");
        assertThat(courseItemResponseDto.getCourseBooks().get(1).getSource()).isEqualTo(BookSource.COUPANG);
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
        assertThatThrownBy(() -> courseItemService.getCourseVideoAndBookList(3L, 1L, 1L, 1L, 1L, null))
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
        assertThatThrownBy(() -> courseItemService.getCourseVideoAndBookList(1L, 1L, 1L, 1L, 1L, loginUser))
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
        assertThatThrownBy(() -> courseItemService.getCourseVideoAndBookList(1L, 1L, 1L, 1L, 1L, loginUser))
                .isInstanceOf(CompanyInfoNotFoundException.class);
    }

    private List<CourseVideoWithLikeDto> makeCourseVideoList() {
        List<CourseVideoWithLikeDto> courseVideoList = new ArrayList<>();
        courseVideoList.add(CourseVideoWithLikeDto.builder().id(2L).title("Java 강의 영상2").likeCnt(100L).courseVideoUserId(1L).build());
        courseVideoList.add(CourseVideoWithLikeDto.builder().id(1L).title("Java 강의 영상1").likeCnt(0L).build());
        return courseVideoList;
    }

    private List<CourseBook> makeCourseBookList() {
        List<CourseBook> courseBookList = new ArrayList<>();
        courseBookList.add(CourseBook.builder().id(1L).title("책1").url("https://책1.com").thumbnail("https://책1_이미지.png").source(BookSource.COUPANG).build());
        courseBookList.add(CourseBook.builder().id(2L).title("책2").url("https://책2.com").thumbnail("https://책2_이미지.png").source(BookSource.COUPANG).build());
        return courseBookList;
    }

    private CompanyInfo makeCompanyInfo() {
        Company company = Company.builder().id(1L).name("test company").build();
        Job job = Job.builder().id(1L).name("test job").build();
        DetailedPosition detailedPosition = DetailedPosition.builder().id(1L).name("test detailed position").build();
        return CompanyInfo.builder().id(1L).company(company).job(job).detailedPosition(detailedPosition).build();
    }
}
