-- 유저
INSERT INTO users(user_id, user_email, user_password, user_role, user_provider, user_name, created_at, updated_at) VALUES (1, 'devridge2023@gmail.com', '$2a$10$R7Epiqggi7d4XtU8bJDDeOod6le0i3VdRf.WjBrTaE65uUZHbvek2', 'ADMIN', 'google', 'DEVRIDGE', '2000-01-01 00:00:00', '2000-01-01 00:00:00');

-- 회사
INSERT INTO company(company_id, company_name, created_at, updated_at) VALUES (1, '토스증권', '2000-01-01 00:00:00', '2000-01-01 00:00:00');

-- 직무
INSERT INTO job(job_id, job_name, created_at, updated_at) VALUES (1, '백엔드', '2000-01-01 00:00:00', '2000-01-01 00:00:00');
INSERT INTO job(job_id, job_name, created_at, updated_at) VALUES (2, '프론트엔드', '2000-01-01 00:00:00', '2000-01-01 00:00:00');

-- 회사_직무
INSERT INTO company_job(company_job_id, company_id, job_id, created_at, updated_at) VALUES (1, 1, 1, '2000-01-01 00:00:00', '2000-01-01 00:00:00');
INSERT INTO company_job(company_job_id, company_id, job_id, created_at, updated_at) VALUES (2, 1, 2, '2000-01-01 00:00:00', '2000-01-01 00:00:00');

-- 서비스
INSERT INTO detailed_position(detailed_position_id, detailed_position_name, company_id, created_at, updated_at) VALUES (1, 'Product', 1, '2000-01-01 00:00:00', '2000-01-01 00:00:00');
INSERT INTO detailed_position(detailed_position_id, detailed_position_name, company_id, created_at, updated_at) VALUES (2, 'Platform', 1, '2000-01-01 00:00:00', '2000-01-01 00:00:00');

-- 직무_서비스
INSERT INTO job_detailed_position(job_detailed_position_id, job_id, detailed_position_id, created_at, updated_at) VALUES (1, 1, 1, '2000-01-01 00:00:00', '2000-01-01 00:00:00');
INSERT INTO job_detailed_position(job_detailed_position_id, job_id, detailed_position_id, created_at, updated_at) VALUES (2, 1, 2, '2000-01-01 00:00:00', '2000-01-01 00:00:00');
INSERT INTO job_detailed_position(job_detailed_position_id, job_id, detailed_position_id, created_at, updated_at) VALUES (3, 2, 1, '2000-01-01 00:00:00', '2000-01-01 00:00:00');
INSERT INTO job_detailed_position(job_detailed_position_id, job_id, detailed_position_id, created_at, updated_at) VALUES (4, 2, 2, '2000-01-01 00:00:00', '2000-01-01 00:00:00');

-- 회사정보
INSERT INTO company_info(company_info_id, company_info_content, company_id, detailed_position_id, job_id, created_at, updated_at) VALUES (1, '토스증권 Product 백엔드 회사정보', 1,1,1 , '2000-01-01 00:00:00', '2000-01-01 00:00:00');
INSERT INTO company_info(company_info_id, company_info_content, company_id, detailed_position_id, job_id, created_at, updated_at) VALUES (2, '토스증권 Product 프론트 회사정보', 1,1,2 , '2000-01-01 00:00:00', '2000-01-01 00:00:00');
INSERT INTO company_info(company_info_id, company_info_content, company_id, detailed_position_id, job_id, created_at, updated_at) VALUES (3, '토스증권 Platform 백엔드 회사정보', 1,2,1 , '2000-01-01 00:00:00', '2000-01-01 00:00:00');
INSERT INTO company_info(company_info_id, company_info_content, company_id, detailed_position_id, job_id, created_at, updated_at) VALUES (4, '토스증권 Platform 프론트 회사정보', 1,2,2 , '2000-01-01 00:00:00', '2000-01-01 00:00:00');

-- 코스
INSERT INTO course(course_id, course_name, course_type, course_order, job_id, created_at, updated_at) VALUES (1, '언어', 'SKILL', 1, 1,  '2000-01-01 00:00:00', '2000-01-01 00:00:00');
INSERT INTO course(course_id, course_name, course_type, course_order, job_id, created_at, updated_at) VALUES (2, '네트워크', 'CS', 2, 1,  '2000-01-01 00:00:00', '2000-01-01 00:00:00');
INSERT INTO course(course_id, course_name, course_type, course_order, job_id, created_at, updated_at) VALUES (3, '프레임워크', 'SKILL', 3, 1,  '2000-01-01 00:00:00', '2000-01-01 00:00:00');
INSERT INTO course(course_id, course_name, course_type, course_order, job_id, created_at, updated_at) VALUES (4, '데이터베이스', 'SKILL', 4, 1,  '2000-01-01 00:00:00', '2000-01-01 00:00:00');
INSERT INTO course(course_id, course_name, course_type, course_order, job_id, created_at, updated_at) VALUES (5, '운영체제', 'CS', 4, 1,  '2000-01-01 00:00:00', '2000-01-01 00:00:00');

-- 로드맵
INSERT INTO roadmap(roadmap_id, company_info_id, roadmap_matching_flag, course_id, created_at, updated_at) VALUES (1, 1,'YES', 1, '2000-01-01 00:00:00', '2000-01-01 00:00:00');
INSERT INTO roadmap(roadmap_id, company_info_id, roadmap_matching_flag, course_id, created_at, updated_at) VALUES (2, 1, 'NO', 3, '2000-01-01 00:00:00', '2000-01-01 00:00:00');
INSERT INTO roadmap(roadmap_id, company_info_id, roadmap_matching_flag, course_id, created_at, updated_at) VALUES (3, 1, 'NO', 2, '2000-01-01 00:00:00', '2000-01-01 00:00:00');
INSERT INTO roadmap(roadmap_id, company_info_id, roadmap_matching_flag, course_id, created_at, updated_at) VALUES (4, 1, 'YES', 4, '2000-01-01 00:00:00', '2000-01-01 00:00:00');
INSERT INTO roadmap(roadmap_id, company_info_id, roadmap_matching_flag, course_id, created_at, updated_at) VALUES (5, 1, 'YES', 5, '2000-01-01 00:00:00', '2000-01-01 00:00:00');

-- 코스 상세
INSERT INTO course_detail(course_detail_id, course_detail_name, created_at, updated_at) VALUES (1, 'Java', '2000-01-01 00:00:00', '2000-01-01 00:00:00');
INSERT INTO course_detail(course_detail_id, course_detail_name, created_at, updated_at) VALUES (2, 'C', '2000-01-01 00:00:00', '2000-01-01 00:00:00');
INSERT INTO course_detail(course_detail_id, course_detail_name, created_at, updated_at) VALUES (3, 'C++', '2000-01-01 00:00:00', '2000-01-01 00:00:00');
INSERT INTO course_detail(course_detail_id, course_detail_name, created_at, updated_at) VALUES (4, 'Python', '2000-01-01 00:00:00', '2000-01-01 00:00:00');
INSERT INTO course_detail(course_detail_id, course_detail_name, created_at, updated_at) VALUES (5, '네트워크 임시1', '2000-01-01 00:00:00', '2000-01-01 00:00:00');
INSERT INTO course_detail(course_detail_id, course_detail_name, created_at, updated_at) VALUES (6, '네트워크 임시2', '2000-01-01 00:00:00', '2000-01-01 00:00:00');
INSERT INTO course_detail(course_detail_id, course_detail_name, created_at, updated_at) VALUES (7, '스프링', '2000-01-01 00:00:00', '2000-01-01 00:00:00');
INSERT INTO course_detail(course_detail_id, course_detail_name, created_at, updated_at) VALUES (8, 'django', '2000-01-01 00:00:00', '2000-01-01 00:00:00');

-- 코스 상세_코스
INSERT INTO course_to_detail(course_to_detail_id, course_id, course_detail_id, created_at, updated_at) VALUES (1, 1, 1, '2000-01-01 00:00:00', '2000-01-01 00:00:00');
INSERT INTO course_to_detail(course_to_detail_id, course_id, course_detail_id, created_at, updated_at) VALUES (2, 1, 2, '2000-01-01 00:00:00', '2000-01-01 00:00:00');
INSERT INTO course_to_detail(course_to_detail_id, course_id, course_detail_id, created_at, updated_at) VALUES (3, 1, 3, '2000-01-01 00:00:00', '2000-01-01 00:00:00');
INSERT INTO course_to_detail(course_to_detail_id, course_id, course_detail_id, created_at, updated_at) VALUES (4, 1, 4, '2000-01-01 00:00:00', '2000-01-01 00:00:00');
INSERT INTO course_to_detail(course_to_detail_id, course_id, course_detail_id, created_at, updated_at) VALUES (5, 2, 5, '2000-01-01 00:00:00', '2000-01-01 00:00:00');
INSERT INTO course_to_detail(course_to_detail_id, course_id, course_detail_id, created_at, updated_at) VALUES (6, 2, 6, '2000-01-01 00:00:00', '2000-01-01 00:00:00');
INSERT INTO course_to_detail(course_to_detail_id, course_id, course_detail_id, created_at, updated_at) VALUES (7, 3, 7, '2000-01-01 00:00:00', '2000-01-01 00:00:00');
INSERT INTO course_to_detail(course_to_detail_id, course_id, course_detail_id, created_at, updated_at) VALUES (8, 3, 8, '2000-01-01 00:00:00', '2000-01-01 00:00:00');




-- 회사 요구 역량
INSERT INTO company_required_ability(company_required_ability_id, company_required_ability_name, course_detail_id, created_at, updated_at) VALUES (1, 'C++', 3, '2000-01-01 00:00:00', '2000-01-01 00:00:00');
INSERT INTO company_required_ability(company_required_ability_id, company_required_ability_name, course_detail_id, created_at, updated_at) VALUES (2, ' DJ AN G O ', 8, '2000-01-01 00:00:00', '2000-01-01 00:00:00');
INSERT INTO company_required_ability(company_required_ability_id, company_required_ability_name, course_detail_id, created_at, updated_at) VALUES (3, ' Java ', 1, '2000-01-01 00:00:00', '2000-01-01 00:00:00');

INSERT INTO company_info_company_required_ability(company_info_company_required_ability_id, company_info_id, company_required_ability_id, created_at, updated_at) VALUES (1, 1, 1, '2000-01-01 00:00:00', '2000-01-01 00:00:00');
INSERT INTO company_info_company_required_ability(company_info_company_required_ability_id, company_info_id, company_required_ability_id, created_at, updated_at) VALUES (2, 1, 2, '2000-01-01 00:00:00', '2000-01-01 00:00:00');
INSERT INTO company_info_company_required_ability(company_info_company_required_ability_id, company_info_id, company_required_ability_id, created_at, updated_at) VALUES (3, 1, 3, '2000-01-01 00:00:00', '2000-01-01 00:00:00');


-- 영상 정보
 INSERT INTO course_video(course_video_id, course_detail_id, course_video_title, course_video_url, course_video_thumbnail, course_video_source, created_at, updated_at)
 VALUES (1, 1, '자바 수업을 리뉴얼 했습니다', 'https://www.youtube.com/watch?v=jdTsJzXmgU0&list=PLuHgQVnccGMCeAy-2-llhw3nWoQKUvQck', 'https://img.youtube.com/vi/jdTsJzXmgU0/0.jpg', 'YOUTUBE', '2000-01-01 00:00:00', '2000-01-01 00:00:00');

 INSERT INTO course_video(course_video_id, course_detail_id, course_video_title, course_video_url, course_video_thumbnail, course_video_source, created_at, updated_at)
 VALUES (2, 1, '[자바의정석-기초편] ch1-1,2 자바란? 자바의 역사', 'https://www.youtube.com/watch?v=oJlCC1DutbA&list=PLW2UjW795-f6xWA2_MUhEVgPauhGl3xIp', 'https://img.youtube.com/vi/oJlCC1DutbA/0.jpg', 'YOUTUBE', '2000-01-01 00:00:00', '2000-01-01 00:00:00');


-- 유저 학습 상태