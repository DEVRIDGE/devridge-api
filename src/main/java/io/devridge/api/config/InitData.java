//package io.devridge.api.config;
//
//import io.devridge.api.domain.company_job.*;
//import io.devridge.api.domain.companyinfo.*;
//import io.devridge.api.domain.course.*;
//import io.devridge.api.domain.employment.*;
//import io.devridge.api.domain.roadmap.Course;
//import io.devridge.api.domain.roadmap.CourseDetail;
//import io.devridge.api.domain.roadmap.CourseDetailRepository;
//import io.devridge.api.domain.roadmap.CourseRepository;
//import io.devridge.api.domain.video.CourseVideo;
//import io.devridge.api.domain.video.CourseVideoRepository;
//import io.devridge.api.domain.video.VideoSource;
//import io.devridge.api.service.EmploymentService;
//import lombok.Getter;
//import lombok.RequiredArgsConstructor;
//import lombok.Setter;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.context.annotation.Profile;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.PostConstruct;
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.time.LocalDate;
//import java.util.*;
//
//@Profile("dev")
//@Slf4j
//@RequiredArgsConstructor
//@Component
//public class InitData {
//
//    private final CompanyRepository companyRepository;
//    private final JobRepository jobRepository;
//    private final CompanyJobRepository companyJobRepository;
//
//    private final CourseRepository courseRepository;
//    private final CourseDetailRepository courseDetailRepository;
//
//    private final EmploymentInfoRepository employmentInfoRepository;
//    private final EmploymentSkillRepository employmentSkillRepository;
//
//    private final EmploymentService employmentService;
//
//    private final CourseVideoRepository courseVideoRepository;
//
//    @PostConstruct
//    public void init() throws IOException {
//        // 회사 초기화
//        Company toss = makeCompany("토스증권");
//
//        // 직무 초기화
//        Job backendJob = makeJob("백엔드");
//        Job frontendJob = makeJob("프론트엔드");
//        Job devopsJob = makeJob("DevOps");
//        Job androidJob = makeJob("Android");
//        Job iosJob = makeJob("iOS");
//
//        // 회사-직무 초기화
//        CompanyJob cj1 = makeCompanyJob(toss, backendJob);
//        CompanyJob cj2 = makeCompanyJob(toss, frontendJob);
//        CompanyJob cj3 = makeCompanyJob(toss, devopsJob);
//        CompanyJob cj4 = makeCompanyJob(toss, androidJob);
//        CompanyJob cj5 = makeCompanyJob(toss, iosJob);
//
//        // 로드맵 초기화
//        Map<String, CourseInfo> stringCourseInfoMap = makeRoadMap("roadmap/backend.csv", backendJob);
//        Map<String, CourseInfo> stringCourseInfoMap1 = makeRoadMap("roadmap/frontend.csv", frontendJob);
//        Map<String, CourseInfo> stringCourseInfoMap2 = makeRoadMap("roadmap/devops.csv", devopsJob);
//        Map<String, CourseInfo> stringCourseInfoMap3 = makeRoadMap("roadmap/android.csv", androidJob);
//        Map<String, CourseInfo> stringCourseInfoMap4 = makeRoadMap("roadmap/ios.csv", iosJob);
//
//
//        // 채용정보 초기화
//        String testEmploymentSkills1 = "Java, Kotlin, Spring Framework, JPA/Hibernate, Netty," +
//                "MySQL, Oracle, Redis, MongoDB, Kafka, Elasticsearch, InfluxDB, Grafana," +
//                "ELK 스택, Tomcat, JVM, OS, 네트워크, 인프라, 성능 튜닝, 실시간 데이터 처리, 네트워크 프로그래밍"; // 토스 >> 토스 증권 >> Platform 데이터
//
//        String testEmploymentSkills2 = "Java, Kotlin, Spring Framework, JPA/Hibernate, Netty," +
//                "MySQL, Oracle, Redis, MongoDB, Kafka, Elasticsearch, InfluxDB, Grafana, Kubernetes, ELK"; // 토스 >> 토스 증권 >> Product 데이터
//
//        // 토스 증권의 백엔드(Server Developer) 직무
//        EmploymentInfo employmentInfo1 = makeEmploymentInfo(toss, backendJob, "토스 증권, Server Developer, Platform 테스트 채용 정보");
//        EmploymentInfo employmentInfo2 = makeEmploymentInfo(toss, backendJob, "토스 증권, Server Developer, Product 테스트 채용 정보");
//
//        List<EmploymentSkill> employmentSkills1 = makeEmploymentSkills(employmentInfo1, testEmploymentSkills1);
//        List<EmploymentSkill> employmentSkills2 = makeEmploymentSkills(employmentInfo2, testEmploymentSkills2);
//
//        // 토스의 백엔드 직무 스킬과 관련된 CourseDetail을 EmploymentSkillCourseDetailRepository에 연관시켜 저장
//        employmentService.saveEmploymentSkillCourseDetailRepository(employmentInfo1.getCompany().getId(), employmentInfo1.getJob().getId());
//
//
//
//        // 비디오 테스트 케이스 추가
//        makeCourseVideo("Java 입문 수업 (생활코딩)",
//                "https://www.youtube.com/watch?v=jdTsJzXmgU0&list=PLuHgQVnccGMCeAy-2-llhw3nWoQKUvQck",
//                "https://i.ytimg.com/vi/jdTsJzXmgU0/hqdefault.jpg?sqp=-oaymwEXCNACELwBSFryq4qpAwkIARUAAIhCGAE=&rs=AOn4CLCT4bu6PPAaGC3galZnG6OzwaQuZg",
//                1, VideoSource.YOUTUBE, courseDetailRepository.findByName("Java"));
//
//        makeCourseVideo("자바의 정석 기초편(2020최신)",
//                "https://www.youtube.com/watch?v=oJlCC1DutbA&list=PLW2UjW795-f6xWA2_MUhEVgPauhGl3xIp",
//                "https://i.ytimg.com/vi/oJlCC1DutbA/hqdefault.jpg?sqp=-oaymwEXCNACELwBSFryq4qpAwkIARUAAIhCGAE=&rs=AOn4CLDwdzPZDDpa0H6LMtRsd7sSDGShwg",
//                10, VideoSource.YOUTUBE, courseDetailRepository.findByName("Java"));
//
//        makeCourseVideo("DATABASE2 - MySQL",
//                "https://www.youtube.com/watch?v=h_XDmyz--0w&list=PLuHgQVnccGMCgrP_9HL3dAcvdt8qOZxjW",
//                "https://i.ytimg.com/vi/h_XDmyz--0w/hqdefault.jpg?sqp=-oaymwEXCNACELwBSFryq4qpAwkIARUAAIhCGAE=&rs=AOn4CLBCcUBhCuE5O9Jr0KxacnPRJ0JN_w",
//                0, VideoSource.YOUTUBE, courseDetailRepository.findByName("MySQL"));
//
//        makeCourseVideo("이것이 MySQL이다",
//                "https://www.youtube.com/watch?v=KLZWDOK8kZM&list=PLVsNizTWUw7HhYtI-4GGmlJ5yxNdwNI_X",
//                "https://i.ytimg.com/vi/KLZWDOK8kZM/hqdefault.jpg?sqp=-oaymwEXCNACELwBSFryq4qpAwkIARUAAIhCGAE=&rs=AOn4CLBY2w_opeKGaxCzQIIFSLzbYVKKTA",
//                100, VideoSource.YOUTUBE, courseDetailRepository.findByName("MySQL"));
//
//    }
//
//    private Company makeCompany(String name) {
//        Company company = new Company(name);
//        return companyRepository.save(company);
//    }
//
//    private Job makeJob(String name) {
//        Job job = new Job(name);
//        return jobRepository.save(job);
//    }
//
//    private CompanyJob makeCompanyJob(Company company, Job job) {
//        CompanyJob companyJob = new CompanyJob(company, job);
//        return companyJobRepository.save(companyJob);
//    }
//
//    private Map<String, CourseInfo> makeRoadMap(String filePath, Job job) throws IOException {
//        List<List<String>> records = getCsv(filePath);
//        Map<String, CourseInfo> roadmap = parseRecords(records);
//        saveToDb(job, roadmap);
//
//        return roadmap;
//    }
//
//
//    private void saveToDb(Job job, Map<String, CourseInfo> roadmap) {
//        // DB에 저장
//        Set<String> set = roadmap.keySet();
//        Iterator<String> iter = set.iterator();
//        int cor = 0; // 좌표 정보
//
//        while (iter.hasNext()) {
//            String key = ((String)iter.next());
//            CourseInfo values = roadmap.get(key);
//            List<String> courseDetails = values.getCourseDetails();
//            Course course = new Course(key, values.getCourseType(), cor++, job); // 좌표 0부터 1씩 증가하도록 저장하였음
//
//            courseRepository.save(course);
//            for(String courseName : courseDetails) {
//                CourseDetail courseDetail = new CourseDetail(courseName, course);
//                courseDetailRepository.save(courseDetail);
//            }
//        }
//    }
//
//    private Map<String, CourseInfo> parseRecords(List<List<String>> records) {
//        // csv 파일 파싱
//        Map<String, CourseInfo> roadmap = new LinkedHashMap<>(); // key: 중분류 문자, value: 세부기술 리스트
//        int courseCnt = records.get(0).size(); // 중분류 개수
//        for(int i = 0; i < courseCnt; i++) {
//            String detailCoursesString = records.get(1).get(i);
//            String[] detailCourses = detailCoursesString.split("\\|");
//            CourseInfo courseInfo = new CourseInfo();
//
//            for(int l = 0; l < detailCourses.length; l++){
//                detailCourses[l] = detailCourses[l].strip();
//            }
//
//            courseInfo.setCourseDetails(Arrays.asList(detailCourses));
//            if(records.get(2).get(i).contains("CS")) { // CS 지식 여부 판단
//                courseInfo.setCourseType(CourseType.CS);
//            }
//            else {
//                courseInfo.setCourseType(CourseType.SKILL);
//            }
//
//            roadmap.put(records.get(0).get(i), courseInfo);
//        }
//        return roadmap;
//    }
//
//    private List<List<String>> getCsv(String filePath) throws IOException {
//        // 로드맵 csv 파일 입력
//        ClassPathResource resource = new ClassPathResource(filePath);
//        BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream()));
//
//        List<List<String>> records = new ArrayList<>(); // 0번째 리스트: 중분류 행, 1번째 리스트: 세부 기술 행, 2번째 리스트: CS 지식 여부
//        String line;
//
//        while((line = br.readLine()) != null) {
//            String[] values = line.split(",");
//            records.add(Arrays.asList(values));
//        }
//        return records;
//    }
//
//    @Getter @Setter
//    class CourseInfo {
//        private List<String> courseDetails;
//        private CourseType courseType;
//    }
//    public EmploymentInfo makeEmploymentInfo(Company company, Job job, String text) {
//        // EmploymentInfo DB 저장
//        EmploymentInfo employmentInfo = new EmploymentInfo(text, LocalDate.now(), company, job);// startDate에는 임시로 현재시간이 저장되도록 함
//        employmentInfoRepository.save(employmentInfo);
//
//        return employmentInfo;
//    }
//
//    public List<EmploymentSkill> makeEmploymentSkills(EmploymentInfo employmentInfo, String skillsStr) {
//        // EmploymentSkill 파싱 후 DB 저장
//        List<EmploymentSkill> employmentSkills = new ArrayList<>();
//        String[] skills = skillsStr.split(",");
//        for(int i = 0; i < skills.length; i++) {
//            skills[i] = skills[i].strip();
//            EmploymentSkill employmentSkill = new EmploymentSkill(skills[i], employmentInfo);
//            employmentSkills.add(employmentSkill);
//            employmentSkillRepository.save(employmentSkill);
//        }
//        return employmentSkills;
//    }
//
//    private CourseVideo makeCourseVideo(String title, String url, String thumbnail, Integer likeCnt, VideoSource source, CourseDetail courseDetail) {
//        CourseVideo courseVideo = new CourseVideo(title, url, thumbnail, likeCnt, source, courseDetail);
//        return courseVideoRepository.save(courseVideo);
//    }
//}
