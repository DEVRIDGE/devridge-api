package io.devridge.api.config;

import io.devridge.api.domain.company_job.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Component
public class InitData {

    private final CompanyRepository companyRepository;
    private final JobRepository jobRepository;
    private final CompanyJobRepository companyJobRepository;

    @PostConstruct
    public void init() throws IOException {
        Company company1 = makeCompany("토스증권");
        Job backendJob = makeJob("백엔드");
        CompanyJob companyJob = makeCompanyJob(company1, job1);

        makeRoadMap("roadmap/backend.csv", backendJob);
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

    private Map<String, List<String>> makeRoadMap(String filePath, Job job) throws IOException {
        List<List<String>> records = getCsv(filePath);
        Map<String, List<String>> roadmap = parseRecords(records);


        return roadmap;
    }

    private static Map<String, List<String>> parseRecords(List<List<String>> records) {
        // csv 파일 파싱
        Map<String, List<String>> roadmap = new LinkedHashMap<>(); // key: 중분류 문자, value: 세부기술 리스트
        int courseCnt = records.get(0).size(); // 중분류 개수
        for(int i = 0; i < courseCnt; i++) {
            String detailCoursesString = records.get(1).get(i);
            String[] detailCourses = detailCoursesString.split("\\|");

            for(int l = 0; l < detailCourses.length; l++){
                detailCourses[l] = detailCourses[l].strip();
            }

            roadmap.put(records.get(0).get(i), Arrays.asList(detailCourses));

            log.info("중분류: {}, 세부기술: {}", records.get(0).get(i), roadmap.get(records.get(0).get(i)));
        }
        return roadmap;
    }

    private static List<List<String>> getCsv(String filePath) throws IOException {
        // 로드맵 csv 파일 입력
        ClassPathResource resource = new ClassPathResource(filePath);
        BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream()));

        List<List<String>> records = new ArrayList<>(); // 0번째 리스트: 중분류 행, 1번째 리스트: 세부 기술 행
        String line;

        while((line = br.readLine()) != null) {
            String[] values = line.split(",");
            records.add(Arrays.asList(values));
        }
        return records;
    }
}
