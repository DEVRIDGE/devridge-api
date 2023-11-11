package io.devridge.api.service.admin;

import io.devridge.api.domain.companyinfo.*;
import io.devridge.api.domain.roadmap.*;
import io.devridge.api.domain.video.CourseVideo;
import io.devridge.api.domain.video.CourseVideoRepository;
import io.devridge.api.dto.admin.CourseDetailInfo;
import io.devridge.api.dto.admin.CourseInfo;
import io.devridge.api.handler.ex.CourseDetailNotFoundException;
import io.devridge.api.handler.ex.CourseNotFoundException;
import io.devridge.api.handler.ex.DeleteFailedExistVideoException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AdminService {

    private final CourseRepository courseRepository;
    private final CourseDetailRepository courseDetailRepository;
    private final CompanyRequiredAbilityRepository companyRequiredAbilityRepository;
    private final CourseVideoRepository courseVideoRepository;

    @Transactional
    public void changeCourse(CourseInfo courseInfo) {
        Course course = courseRepository.findById(courseInfo.getId()).orElseThrow(() -> new CourseNotFoundException("해당하는 코스를 찾을 수 없습니다."));
        course.changeCourseInfo(courseInfo);
    }

    @Transactional
    public void changeCourseDetail(CourseDetailInfo courseDetailInfo) {
        CourseDetail courseDetail = courseDetailRepository.findById(courseDetailInfo.getId()).orElseThrow(() -> new CourseDetailNotFoundException("해당하는 코스 상세를 찾을 수 없습니다."));
        courseDetail.changeCourseDetailInfo(courseDetailInfo);
    }

    @Transactional
    public void deleteCourseDetail(Long courseDetailId) {
        checkCourseVideoAndRequiredAbilityNull(courseDetailId);
        courseDetailRepository.deleteById(courseDetailId);
    }

    private void checkCourseVideoAndRequiredAbilityNull(Long courseDetailId) {
        List<CourseVideo> courseVideoList = courseVideoRepository.findByCourseDetailId(courseDetailId);
        if(!courseVideoList.isEmpty()) {
            throw new DeleteFailedExistVideoException();
        }
        companyRequiredAbilityRepository.findByCourseDetailId(courseDetailId)
                .forEach(companyRequiredAbility -> companyRequiredAbility.changeCourseDetail(null));
    }

//    @Transactional
//    public void makeRoadmap() {
//        List<CompanyInfoIdDto> allCompanyInfo = companyInfoRepository.findByAllIdWithRoadmap();
//
//        for (CompanyInfoIdDto companyInfoIdDto : allCompanyInfo) {
//            List<Long> requriedDetailIdList = companyRequiredAbilityRepository.findIdsByCompanyInfoId(companyInfoIdDto.getCompanyInfoId());
//
//            if(companyInfoIdDto.getRoadmapId() == null) {
//                // 로드맵이 존재하지 않는 경우
//                List<Roadmap> roadmapList = new ArrayList<>();
//
//                List<Course> courseList = courseRepository.getCourseListByJob(companyInfoIdDto.getJobId());
//
//                // 로드맵에 모든 코스를 추가해야해서 반복문 돌리는데
//                for (Course course : courseList) {
//
//                    MatchingFlag matchingFlag = checkMatched(course, requriedDetailIdList);
//                    Roadmap roadmap = Roadmap.builder().
//                            course(course).
//                            matchingFlag(matchingFlag).
//                            companyInfo(CompanyInfo.builder().id(companyInfoIdDto.getCompanyInfoId()).build()).
//                            build();
//                    roadmapList.add(roadmap);
//                }
//                roadmapRepository.saveAll(roadmapList);
//            }
//        }
//    }



//    private Long courseDetailCompareAbilityName(List<Course> courseList, String abilityName) {
//        for (Course course : courseList) {
//            List<CourseDetail> courseDetailList = courseDetailRepository.findByCourseIdOrderByName(course.getId());
//            for (CourseDetail courseDetail : courseDetailList) {
//                if(abilityName.equals(processStringCutting(courseDetail.getName()))) {
//                    return courseDetail.getId();
//                }
//            }
//        }
//        return null;
//    }

//    private MatchingFlag checkMatched(Course course, List<Long> requriedDetailIdList) {
//        MatchingFlag matchingFlag = MatchingFlag.NO;
//
//        List<CourseDetail> courseDetailList = courseDetailRepository.findByCourseIdOrderByName(course.getId());
//        for (CourseDetail courseDetail : courseDetailList) {
//            if(requriedDetailIdList.contains(courseDetail.getId())) {
//                matchingFlag = MatchingFlag.YES;
//                break;
//            }
//        }
//        return matchingFlag;
//    }
}
