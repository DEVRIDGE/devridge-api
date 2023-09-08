package io.devridge.api.service;

import io.devridge.api.domain.companyinfo.Company;
import io.devridge.api.domain.companyinfo.DetailedPosition;
import io.devridge.api.domain.companyinfo.Job;
import io.devridge.api.domain.roadmap.CourseDetail;
import io.devridge.api.domain.video.CourseVideo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class DetailedPositionServiceTest {
    @InjectMocks
    private DetailedPositionService detailedPositionService;

    @DisplayName("회사와 직무가 주어지면 해당 detailedPostion 리스트를 정상적으로 반환한다")
    @Test
    public void getDetailedPositionList_success_test() {
        //given
        Company company = Company.builder().id(1L).name("토스").build();
        Job job = Job.builder().id(1L).name("백엔드").build();
        DetailedPosition detailedPosition1 = DetailedPosition.builder().id(1L).name("Product").company(company).build();
        DetailedPosition detailedPosition2 = DetailedPosition.builder().id(2L).name("Platform").company(company).build();


        //stub
        //when
        //then
        throw new RuntimeException();
    }

    private List<DetailedPosition> makeDetailedPositionList(Company company) {
        List<DetailedPosition> courseVideoList = new ArrayList<>();
        courseVideoList.add(CourseVideo.builder().id(1L).title("Java 강의 영상1").courseDetail(courseDetail).build());
        courseVideoList.add(CourseVideo.builder().id(2L).title("Java 강의 영상2").courseDetail(courseDetail).build());
        return courseVideoList;
    }
}
