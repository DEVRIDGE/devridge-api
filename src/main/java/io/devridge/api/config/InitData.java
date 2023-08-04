package io.devridge.api.config;

import io.devridge.api.domain.company_job.*;
import io.devridge.api.domain.course.*;
import io.devridge.api.domain.employment.EmploymentInfo;
import io.devridge.api.domain.employment.EmploymentSkill;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Component
public class InitData {

    private final CompanyRepository companyRepository;
    private final JobRepository jobRepository;
    private final CompanyJobRepository companyJobRepository;

    private final CourseRepository courseRepository;
    private final CourseDetailRepository courseDetailRepository;

    @PostConstruct
    public void init() throws IOException {
        // 회사 초기화
        Company toss = makeCompany("토스증권");

        // 직무 초기화
        Job backendJob = makeJob("백엔드");
        Job frontendJob = makeJob("프론트엔드");
        Job devopsJob = makeJob("DevOps");
        Job androidJob = makeJob("Android");
        Job iosJob = makeJob("iOS");

        // 회사-직무 초기화
        CompanyJob cj1 = makeCompanyJob(toss, backendJob);
        CompanyJob cj2 = makeCompanyJob(toss, frontendJob);
        CompanyJob cj3 = makeCompanyJob(toss, devopsJob);
        CompanyJob cj4 = makeCompanyJob(toss, androidJob);
        CompanyJob cj5 = makeCompanyJob(toss, iosJob);



        // 로드맵 초기화
        makeRoadMap("roadmap/backend.csv", backendJob);
        makeRoadMap("roadmap/frontend.csv", frontendJob);
        makeRoadMap("roadmap/devops.csv", devopsJob);
        makeRoadMap("roadmap/android.csv", androidJob);
        makeRoadMap("roadmap/ios.csv", iosJob);



        // 채용정보 초기화
        String testRawText = "토스증권 소속\n" +
                "정규직\n" +
                "합류하게 될 팀에 대해 알려드려요\n" +
                "\n" +
                "Server Developer (Platform)는 서버 플랫폼팀과 시세팀에 속해 업무해요.\n" +
                "토스는 보통 작은 feature 단위의 사일로 조직으로 기획자, 디자이너, 개발자들이 소규모로 모여 서비스 제품이 만들어 지는데요, 서버 플랫폼 팀은 이런 사일로 조직들에서 필요로 하는 공통 기능들, 프레임워크를 만드는 팀이에요. 서버 플랫폼팀의 올해 목표는 증권 서비스들이 더 쉽고 빠르게 런칭되고 안정적으로 운영될 수 있도록 하는 것인데요, 앞으로도 많은 서비스들을 확장해 나가려고 해요.\n" +
                "시세팀은 국내주식과 해외주식의 시세 제공과 대외접속(FEP) 등 증권서비스에 필요한 다양한 업무를 수행하고 있는 팀입니다. 시세팀의 올해 목표는 최적화된 초고효율 시스템을 제공하는 것인데요. 앞으로 토스증권의 개발자가 가장 오고 싶어하는 팀이 되어보려 해요.\n" +
                "두 팀은 사일로 조직과는 다르게 기획자가 따로 있는 것이 아닙니다, 팀에 소속된 엔지니어들이 곧 기획자이자 개발자인데요. 어떤 기능들이 필요할지 직접 고민하고 의견을 수집해서 방향성을 정하고 만들어 나가고 있어요.\n" +
                "합류하면 함께 할 업무예요\n" +
                "\n" +
                "다른 개발자들이 불편한 것들을 모아서 공통화하고, 요구사항을 모아 새로운 기능을 추가하고, 여러 영역에 걸쳐 발생하는 문제를 해결하는 역할을 하고 있어요.\n" +
                "토스 증권에서는 서버 개발 환경으로 Java/Kotlin, Spring Framework를 많이 사용하고 있어요. 따라서 관련된 작업들을 많이 하게 되는데요, 다양한 라이브러리와 개발 방식 중에서 합리적이고 효율적인 방식을 찾아 선택하고 공통화시키는 작업을 해요.\n" +
                "또한, 전체적인 관점에서 좋은 아키텍처에 대한 고민을 하고 개선해 나가는 작업을 하는데요. 배포하기 쉬운 구조, 운영하기 쉬운 구조, 안정성을 위한 구조, 성능 개선을 위한 구조에 대해서 고민하고 실행에 옮겨요.\n" +
                "이런 분과 함께하고 싶어요\n" +
                "\n" +
                "Java/Kotlin, Spring Framework에 능숙하신 분이면 좋아요.\n" +
                "Redis, Kafka, ELK 스택 운영경험까지 있으면 더 좋아요.\n" +
                "Spring Framework, Tomcat, JVM, OS, 네트워크, 인프라등 다양한 레이어에서의 트러블슈팅 경험, 그를 위한 모니터링과 나아가서 성능 튜닝 경험도 가지고 있다면 더 좋아요.\n" +
                "실시간 데이터 처리, 네트워크 프로그래밍 경험이 있는 분이면 더 좋아요.\n" +
                "변화를 두려워하지 않고 새로운 기술을 도입하여 서비스와 인프라 환경을 꾸준히 발전시키려는 분과 함께 하고 싶어요.\n" +
                "이력서는 이렇게 작성하시는 걸 추천해요\n" +
                "\n" +
                "경험들을 단순히 나열하기 보다는 경험속에서 얻은 배움들을 확인하고 싶어요. 특히 서비스장애경험이나 어려웠던 일들에 대해서 더 듣고 싶은데요, 어떻게 해결하고 어떤 배움을 얻었는지 알려주시면 좋아요.\n" +
                "플랫폼적인 업무(공통 서비스, 공통 라이브러리 등)를 해본 경험이 있다면 기술해 주세요. 공통 기능을 스스로 도출해서 만들어 보신 경험도 있는지 궁금해요.\n" +
                "서버를 개발하다보면 빠질 수 없는 모니터링과 알림 어떻게 하셨나요? 불편한 점은 없었나요? 관련된 경험들이 있으면 기술해 주세요.\n" +
                "토스증권이 사용하는 기술\n" +
                "\n" +
                "Java, Kotlin, Spring Framework, JPA/Hibernate, Netty\n" +
                "MySQL, Oracle, Redis, MongoDB, Kafka, Elasticsearch, InfluxDB, Grafana\n" +
                "토스증권으로의 합류여정\n" +
                "\n" +
                "서류접수 > 직무 인터뷰 > 문화적합성 인터뷰 > 레퍼런스 체크 > 처우 협의 > 최종합격 및 입사\n" +
                "레퍼런스 체크는 2023년 7월 1일 00시 지원자부터 적용돼요.\n" +
                "기타사항\n" +
                "\n" +
                "이력서 및 제출 서류에 허위 사실이 발견되거나 근무 이력 중 징계사항이 확인될 경우, 채용이 취소될 수 있어요.\n" +
                "토스증권 내규에 따라 채용 금지자 또는 결격사유 해당자는 채용이 취소될 수 있어요.\n" +
                "Server Developer(Platform) 포지션으로 합격하면 플랫폼 팀에 우선적으로 배치돼요. 다만 회사의 우선순위와 입사예정자분의 역량의 특성 등을 종합적으로 고려해 다른 조직으로의 배치를 제안드릴 수 있는 점 참고 부탁드려요.\n" +
                "함께 할 동료를 위한 한마디\n" +
                "\n" +
                "\"현재 서버 플랫폼 팀은 토스증권 내부 개발자의 개발환경과 경험을 더 좋게 만들기 위한 업무를 하고 있습니다.\"\n" +
                "\n" +
                "토스증권에 합류한 지 5개월 정도 되었는데, 짧은 시간동안 많은 성장을 할 수 있었어요. 개발 속도를 높이면서도, 보안면이나 서비스 장애에 해를 끼치지 않기 위한 환경을 제공하고 있습니다.\n" +
                "저희 팀에는 항상 서비스에 대한 고민과 성장을 위해 노력하시는 분들이 모여 있어요. 그만큼 저에게 성장 기회를 많이 주는 것 같아요. 이런 분위기에서 자유로운 의견과 토론을 하면서 개발하고 싶은 분들이 오시면 좋을 것 같아요 .";

        String testEmploymentSkills = "Java, Kotlin, Spring Framework, JPA/Hibernate, Netty," +
                "MySQL, Oracle, Redis, MongoDB, Kafka, Elasticsearch, InfluxDB, Grafana," +
                "ELK 스택, Tomcat, JVM, OS, 네트워크, 인프라, 성능 튜닝, 실시간 데이터 처리, 네트워크 프로그래밍"; // 토스 >> 토스 증권 >> Platform 데이터

        makeEmployment(toss, backendJob, testRawText, testEmploymentSkills);
    }



    private Company makeCompany(String name) {
        Company company = new Company(name, "");
        return companyRepository.save(company);
    }

    private Job makeJob(String name) {
        Job job = new Job(name);
        return jobRepository.save(job);
    }

    private CompanyJob makeCompanyJob(Company company, Job job) {
        CompanyJob companyJob = new CompanyJob(company, job);
        return companyJobRepository.save(companyJob);
    }

    private Map<String, CourseInfo> makeRoadMap(String filePath, Job job) throws IOException {
        List<List<String>> records = getCsv(filePath);
        Map<String, CourseInfo> roadmap = parseRecords(records);
        saveToDb(job, roadmap);

        return roadmap;
    }


    private void saveToDb(Job job, Map<String, CourseInfo> roadmap) {
        // DB에 저장
        Set<String> set = roadmap.keySet();
        Iterator<String> iter = set.iterator();
        int cor = 0; // 좌표 정보

        while (iter.hasNext()) {
            String key = ((String)iter.next());
            CourseInfo values = roadmap.get(key);
            List<String> courseDetails = values.getCourseDetails();
            Course course = new Course(key, values.getCourseType(), String.valueOf(cor++), job); // 좌표 0부터 1씩 증가하도록 저장하였음

            courseRepository.save(course);
            for(String courseName : courseDetails) {
                CourseDetail courseDetail = new CourseDetail(courseName, course);
                courseDetailRepository.save(courseDetail);
            }
        }
    }

    private Map<String, CourseInfo> parseRecords(List<List<String>> records) {
        // csv 파일 파싱
        Map<String, CourseInfo> roadmap = new LinkedHashMap<>(); // key: 중분류 문자, value: 세부기술 리스트
        int courseCnt = records.get(0).size(); // 중분류 개수
        for(int i = 0; i < courseCnt; i++) {
            String detailCoursesString = records.get(1).get(i);
            String[] detailCourses = detailCoursesString.split("\\|");
            CourseInfo courseInfo = new CourseInfo();

            for(int l = 0; l < detailCourses.length; l++){
                detailCourses[l] = detailCourses[l].strip();
            }

            courseInfo.setCourseDetails(Arrays.asList(detailCourses));
            if(records.get(2).get(i).contains("CS")) { // CS 지식 여부 판단
                courseInfo.setCourseType(CourseType.CS);
            }
            else {
                courseInfo.setCourseType(CourseType.SKILL);
            }

            roadmap.put(records.get(0).get(i), courseInfo);

//            log.info("중분류: {}, 세부기술: {}, CS 여부: {}", records.get(0).get(i),
//                    roadmap.get(records.get(0).get(i)).getCourseDetails(), roadmap.get(records.get(0).get(i)).getCourseType());
        }
        return roadmap;
    }

    private List<List<String>> getCsv(String filePath) throws IOException {
        // 로드맵 csv 파일 입력
        ClassPathResource resource = new ClassPathResource(filePath);
        BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream()));

        List<List<String>> records = new ArrayList<>(); // 0번째 리스트: 중분류 행, 1번째 리스트: 세부 기술 행, 2번째 리스트: CS 지식 여부
        String line;

        while((line = br.readLine()) != null) {
            String[] values = line.split(",");
            records.add(Arrays.asList(values));
        }
        return records;
    }

    @Getter @Setter
    class CourseInfo {
        private List<String> courseDetails;
        private CourseType courseType;
    }

    private void makeEmployment(Company company, Job job, String text, String skills) {
        // EmploymentInfo
        EmploymentInfo employmentInfo = new EmploymentInfo(text, LocalDate.now(), company, job);// startDate에는 임시로 현재시간이 저장되도록 함

        // EmploymentSkill

        // EmplymentSkillCourseDetail

    }
}
