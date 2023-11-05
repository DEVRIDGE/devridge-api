package io.devridge.api.domain.coursevideo;

import io.devridge.api.config.JpaConfig;
import io.devridge.api.domain.roadmap.CourseDetail;
import io.devridge.api.domain.roadmap.CourseDetailRepository;
import io.devridge.api.domain.user.User;
import io.devridge.api.domain.user.UserRepository;
import io.devridge.api.domain.video.CourseVideo;
import io.devridge.api.domain.video.CourseVideoRepository;
import io.devridge.api.domain.video.CourseVideoUser;
import io.devridge.api.domain.video.CourseVideoUserRepository;
import io.devridge.api.dto.item.CourseVideoWithLikeDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@Import(JpaConfig.class)
@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class CourseVideoRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseDetailRepository courseDetailRepository;

    @Autowired
    private CourseVideoRepository courseVideoRepository;

    @Autowired
    private CourseVideoUserRepository courseVideoUserRepository;

    @Autowired
    private EntityManager em;

    @DisplayName("코스 상세와 매칭되는 영상들을 좋아요 순으로 반환한다.")
    @Test
    void get_course_detail_list_order_by_like_cnt_success_test() {
        User testUser = userRepository.save(User.builder()
                .name("testUser")
                .build());

        CourseDetail courseDetail = courseDetailRepository.save(CourseDetail.builder()
                .name("testCourseDetail1")
                .build());

        CourseVideo testCourseVideo1 = courseVideoRepository.save(CourseVideo.builder()
                .title("testCourseVideo1")
                .courseDetail(courseDetail)
                .build());

        CourseVideo testCourseVideo2 = courseVideoRepository.save(CourseVideo.builder()
                .title("testCourseVideo2")
                .courseDetail(courseDetail)
                .build());

        CourseVideoUser courseVideoUser = courseVideoUserRepository.save(CourseVideoUser.builder()
                .user(testUser)
                .courseVideo(testCourseVideo2)
                .build());

        em.flush();
        em.clear();

        List<CourseVideoWithLikeDto> courseVideoWithLikeDtoList = courseVideoRepository.findWithLikeCntByCourseDetailIdOrderByLikeCntDesc(courseDetail.getId(), 1L);

        assertThat(courseVideoWithLikeDtoList.get(0).getTitle()).isEqualTo("testCourseVideo2");
        assertThat(courseVideoWithLikeDtoList.get(0).getLikeCnt()).isEqualTo(1L);
        assertThat(courseVideoWithLikeDtoList.get(1).getTitle()).isEqualTo("testCourseVideo1");
        assertThat(courseVideoWithLikeDtoList.get(1).getLikeCnt()).isEqualTo(0L);
    }
}
