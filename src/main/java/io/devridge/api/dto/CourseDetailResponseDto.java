package io.devridge.api.dto;

import io.devridge.api.domain.roadmap.CourseDetail;
import lombok.Getter;

import java.util.*;
import java.util.stream.Collectors;

@Getter
public class CourseDetailResponseDto {
    private String title;
    private List<CourseDetailDto> courseDetails;

    public CourseDetailResponseDto(String title, List<CourseDetail> courseDetailList) {
        this.title = title;

        List<CourseDetailDto> DuplicatedList = courseDetailList.stream().map(cd -> new CourseDetailDto(cd)).collect(Collectors.toList());
        List<CourseDetailDto> notDuplicatedList = new ArrayList<>();

        for(CourseDetailDto cd : DuplicatedList) { // 중복 제거
            if(!notDuplicatedList.contains(cd)){
                notDuplicatedList.add(cd);
            }
        }

        notDuplicatedList = notDuplicatedList = notDuplicatedList.stream()
                                            .sorted(Comparator.comparing(CourseDetailDto::getName)).collect(Collectors.toList()); // 이름순 정렬
        this.courseDetails = notDuplicatedList;
    }

    @Getter
    public class CourseDetailDto {
        private Long id;
        private String name;

        public CourseDetailDto(CourseDetail courseDetail) {
            this.id = courseDetail.getId();
            this.name = courseDetail.getName();
        }

        public boolean equals(Object obj) { // 두 CourseDetailDto가 있을 때, id가 같으면 같은 객체로 판단함
            if(obj instanceof CourseDetailDto)
                return id==((CourseDetailDto)obj).getId();
            else
                return false;
        }
    }

}
