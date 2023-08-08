CREATE TABLE users (
    user_id bigint auto_increment PRIMARY KEY,
    user_email varchar(255),
    user_picture varchar(255),
    user_role varchar(255),
    user_provider varchar(255),
    user_provider_id varchar(255),
    created_at timestamp NOT NULL,
    updated_at timestamp NOT NULL
);

CREATE TABLE company (
     company_id bigint auto_increment PRIMARY KEY,
     company_name varchar(255),
     company_logo varchar(255),
     created_at timestamp NOT NULL,
     updated_at timestamp NOT NULL
);

CREATE TABLE job (
    job_id bigint auto_increment PRIMARY KEY,
    job_name varchar(255),
    created_at timestamp NOT NULL,
    updated_at timestamp NOT NULL
)

CREATE TABLE company_job (
    company_job_id bigint auto_increment PRIMARY KEY,
    company_id bigint,
    job_id bigint,
    created_at timestamp NOT NULL,
    updated_at timestamp NOT NULL,
    FOREIGN KEY (company_id) REFERENCES company(company_id),
    FOREIGN KEY (job_id) REFERENCES job(job_id)
);

CREATE TABLE course (
    course_id bigint auto_increment PRIMARY KEY,
    course_name varchar(255),
    course_turn integer,
    course_type varchar(255),
    job_id bigint,
    created_at timestamp NOT NULL,
    updated_at timestamp NOT NULL,
    FOREIGN KEY (job_id) REFERENCES job(job_id)
);

CREATE TABLE course_user (
    course_user_id bigint auto_increment PRIMARY KEY,
    course_study_status varchar(255),
    course_id bigint,
    user_id bigint,
    created_at timestamp NOT NULL,
    updated_at timestamp NOT NULL,
    FOREIGN KEY (course_id) REFERENCES course(course_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

CREATE TABLE course_detail (
    course_detail_id bigint auto_increment PRIMARY KEY,
    course_detail_name varchar(255),
    course_id bigint,
    created_at timestamp NOT NULL,
    updated_at timestamp NOT NULL,
    FOREIGN KEY (course_id) REFERENCES course(course_id)
);

CREATE TABLE course_video (
    course_video_id bigint auto_increment PRIMARY KEY,
    course_video_title varchar(255),
    course_video_url varchar(255),
    course_video_thumbnail varchar(255),
    course_video_source varchar(255),
    course_video_like_cnt integer,
    course_detail_id bigint,
    created_at timestamp NOT NULL,
    updated_at timestamp NOT NULL,
    FOREIGN KEY (course_detail_id) REFERENCES course_detail(course_detail_id)
);

CREATE TABLE course_video_user (
    course_video_user_id bigint auto_increment PRIMARY KEY,
    course_video_id bigint,
    user_id bigint,
    created_at timestamp NOT NULL,
    updated_at timestamp NOT NULL,
    FOREIGN KEY (course_video_id) REFERENCES course_video(course_video_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

CREATE TABLE employment_info (
    employment_info_id bigint auto_increment PRIMARY KEY,
    employment_info_text varchar(255),
    employment_info_start_date date,
    company_id bigint,
    job_id bigint,
    created_at timestamp NOT NULL,
    updated_at timestamp NOT NULL,
    FOREIGN KEY (company_id) REFERENCES company(company_id),
    FOREIGN KEY (job_id) REFERENCES job(job_id)
);

CREATE TABLE employment_skill (
    employment_skill_id bigint auto_increment PRIMARY KEY,
    employment_skill_name varchar(255),
    employment_info_id bigint,
    created_at timestamp NOT NULL,
    updated_at timestamp NOT NULL,
    FOREIGN KEY (employment_info_id) REFERENCES employment_info(employment_info_id)
);

CREATE TABLE employment_skill_course_detail (
    employment_skill_course_detail_id bigint auto_increment PRIMARY KEY,
    course_detail_id bigint,
    employment_skill_id bigint,
    created_at timestamp NOT NULL,
    updated_at timestamp NOT NULL,
    FOREIGN KEY (course_detail_id) REFERENCES course_detail(course_detail_id),
    FOREIGN KEY (employment_skill_id) REFERENCES employment_skill(employment_skill_id)
);

CREATE TABLE post (
    post_id bigint auto_increment PRIMARY KEY,
    post_content varchar(255),
    post_type varchar(255),
    user_id bigint,
    created_at timestamp NOT NULL,
    updated_at timestamp NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);


