package io.devridge.api.service;


import java.util.*;
//
//@Slf4j
//@Service
//@Transactional
//@RequiredArgsConstructor
//public class EmploymentService {
//    private final EmploymentSkillCourseDetailRepository employmentSkillCourseDetailRepository;
//
//    /**
//     * 채용스킬과 대응되는 로드맵 상세기술을 찾아서 EmploymentSkillCourseDetailRepository에 저장하는 로직
//     */
//    public void saveEmploymentSkillCourseDetailRepository(long companyId, long jobId) {
//        List<EmploymentSkillCourseDetail> employmentSkillAndCourseDetailList =
//                employmentSkillCourseDetailRepository.findEmploymentSkillAndCourseDetailListByCompanyIdAndJobId(companyId, jobId);
//
//        for(EmploymentSkillCourseDetail escd : employmentSkillAndCourseDetailList)  {
//            employmentSkillCourseDetailRepository.save(escd);
//        }
//    }
//}
