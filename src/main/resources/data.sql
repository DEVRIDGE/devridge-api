-- 회사
INSERT INTO company(company_name, created_at, updated_at) VALUES ('토스증권', '2000-01-01 00:00:00', '2000-01-01 00:00:00');

-- 직무
INSERT INTO job(job_name, created_at, updated_at) VALUES ('백엔드', '2000-01-01 00:00:00', '2000-01-01 00:00:00');

-- 서비스
INSERT INTO detailed_position(detailed_position_name, company_id, created_at, updated_at) VALUES ('Product', 1,  '2000-01-01 00:00:00', '2000-01-01 00:00:00');

-- 코스
INSERT INTO course(course_name, course_type, course_order, job_id, created_at, updated_at) VALUES ('언어', 'SKILL', 1, 1,  '2000-01-01 00:00:00', '2000-01-01 00:00:00');
INSERT INTO course(course_name, course_type, course_order, job_id, created_at, updated_at) VALUES ('네트워크', 'CS', 2, 1,  '2000-01-01 00:00:00', '2000-01-01 00:00:00');
INSERT INTO course(course_name, course_type, course_order, job_id, created_at, updated_at) VALUES ('프레임워크', 'SKILL', 3, 1,  '2000-01-01 00:00:00', '2000-01-01 00:00:00');

-- 회사정보
INSERT INTO company_info(company_info_content, company_id, detailed_position_id, job_id, created_at, updated_at) VALUES ('토스증권 회사정보1', 1,1,1 , '2000-01-01 00:00:00', '2000-01-01 00:00:00');

-- 로드맵
INSERT INTO roadmap(company_info_id, roadmap_matching_flag, course_id, created_at, updated_at) VALUES (1,'YES', 1,  '2000-01-01 00:00:00', '2000-01-01 00:00:00');
INSERT INTO roadmap(company_info_id, roadmap_matching_flag, course_id, created_at, updated_at) VALUES (1, 'NO', 3, '2000-01-01 00:00:00', '2000-01-01 00:00:00');
INSERT INTO roadmap(company_info_id, roadmap_matching_flag, course_id, created_at, updated_at) VALUES (1, 'NO', 2, '2000-01-01 00:00:00', '2000-01-01 00:00:00');
