-- Teacher Entity
insert into teacher_tb(id, name, created_at, sign)
values (1, '최주호', now(),
        null),
       (2, '김근호', now(),
        null),
       (3, '정동진', now(),
        null),
       (4, '윤종두', now(),
        null);

-- User Entity
insert into user_tb(id, username, password, email, role, student_id, teacher_id, created_at)
values (1, 'ssar', '1234', 'ssar@nate.com', 'TEACHER', null, 1, now()),
       (7, 'groom', '1234', 'groom@nate.com', 'TEACHER', null, 2, now()),
       (8, 'simba', '1234', 'simba@nate.com', 'TEACHER', null, 3, now()),
       (9, 'hongyun', '1234', 'hongyun@nate.com', 'TEACHER', null, 4, now());


-- Course Entity
insert into course_tb(id, code, course_status, start_date, end_date, level, purpose, round, title, total_day,
                      total_time, created_at)
values (1, 'A1001', 'NOT_STARTED', '2025-08-01', '2025-12-31', 5, '마이크로 아키텍쳐에 대해서 이해한다.', 1, 'MSA기반 자바과정', 150, 1200,
        now());

-- CourseTeacher Entity
insert into course_teacher_tb(id, course_id, teacher_id, teacher_type, created_at)
values (1, 1, 1, 'MAIN', now()),
       (2, 1, 2, 'SUB', now()),
       (3, 1, 3, 'SUB', now());

-- Student Entity (course_id 제거 - Entity에 없음)
insert into student_tb(id, name, birthday, drop_out_date, drop_out_reason, comment,
                       grade_level, student_status, auth_code, is_verified, created_at)
values (1, '코스', '100000', null, null, null,
        null, 'ENROLL', null, true, now()),

       (2, '러브', '200000', null, null, null,
        null, 'ENROLL', null, true, now()),

       (3, '하하', '300000', null, null, null,
        null, 'ENROLL', null, true, now()),

       (4, '취업이', '400000', '2025-06-20', '취업을 하였습니다.', null,
        null, 'EMPLOY', null, true, now()),

       (5, '탈락이', '500000', '2025-06-20', '개인사정으로 중도탈락 하였습니다.', null,
        null, 'DROPOUT', null, true, now());

insert into user_tb(id, username, password, email, role, student_id, teacher_id, created_at)
values (2, 'cos', '1234', 'cos@nate.com', 'STUDENT', 1, null, now()),
       (3, 'love', '1234', 'love@nate.com', 'STUDENT', 2, null, now()),
       (4, 'haha', '1234', 'haha@nate.com', 'STUDENT', 3, null, now()),
       (5, 'readycompany', '1234', 'readycompany@nate.com', 'STUDENT', 4, null, now()),
       (6, 'tal', '1234', 'tal@nate.com', 'STUDENT', 5, null, now());

-- CourseStudent Entity
insert into course_student_tb(id, course_id, student_id, created_at)
values (1, 1, 1, now()),
       (2, 1, 2, now()),
       (3, 1, 3, now()),
       (4, 1, 4, now()),
       (5, 1, 5, now());

-- Subject Entity (teacher_id 제거 - Entity에 없음)
insert into subject_tb(id, course_id, no, code, title, purpose,
                       ncs_type, grade_level, total_time, learning_way,
                       score_policy, start_date, end_date, created_at)
values (1, 1, 1, 'S2001', '자바', '객체지향을 학습하는 능력이다',
        'NCS', 3, 50, 'THEORY',
        0.9, '2025-06-20', '2025-06-21', now()),

       (2, 1, 2, 'S2002', '스프링부트', 'HTTP를 학습하는 능력이다',
        'NCS', 3, 50, 'MIXED',
        0.9, '2025-06-22', '2025-06-23', now()),

       (3, 1, 3, 'S1001', '미니프로젝트1', '스프링, 템플릿 엔진, 협업능력을 보는 프로젝트이다.',
        'NON_NCS', 3, 120, 'PRACTICE',
        0.9, '2025-06-24', '2025-06-25', now());


-- SubjectElement Entity
insert into subject_element_tb(id, no, title, criterion, subject_id, created_at)
values (1, 1, '연산자', '다양한 연산자 활용을 통해 유연한 코드 구현이 가능하다.', 1, now()),
       (2, 2, '객체지향', '객체지향 원칙을 적용하여 유지보수성과 확장성이 높은 코드를 작성할 수 있다.', 1, now()),

       (3, 1, 'DI', '의존성 주입 개념을 이해하고, 이를 적용한 컴포넌트 간의 결합도 감소를 구현할 수 있다.', 2, now()),
       (4, 2, 'IoC', '제어의 역전을 이해하고 IoC 컨테이너를 활용한 컴포넌트 제어가 가능하다.', 2, now()),
       (5, 3, '어노테이션', '어노테이션 기반 프로그래밍을 이해하고 실무에 적용할 수 있다.', 2, now()),
       (6, 4, '리플렉션', '리플렉션 API를 활용하여 런타임 동적 객체 제어가 가능하다.', 2, now()),
       (7, 5, '컨트롤러', 'MVC 패턴에서 컨트롤러의 역할을 이해하고 효과적으로 구현할 수 있다.', 2, now()),

       (8, 1, '백엔드 구현 능력', '요구사항을 기반으로 RESTful API를 설계하고 구현할 수 있다.', 3, now()),
       (9, 2, '주소 설계가 가능하다.', 'HTTP 메서드와 REST 규칙에 맞춘 URL 설계가 가능하다.', 3, now()),
       (10, 3, '커뮤니케이션능력', '개발 과정에서 팀원과 효과적으로 소통하고 협업할 수 있다.', 3, now());

-- Paper Entity
insert into paper_tb(id, evaluation_way, evaluation_date, subject_id, paper_version,
                     evaluation_room, evaluation_device, max_score,
                     task_title, task_scenario, task_scenario_guide_link,
                     task_submit_format, task_submit_template_link, task_challenge,
                     created_at)
values
-- MCQ 본평가
(1, 'MCQ', '2025-06-21', 1, 'ORIGINAL',
 '본관 3층 302호', '인터넷 가능한 PC', null,
 null, null, null,
 null, null, null,
 now()),

-- MCQ 본평가
(2, 'MCQ', '2025-06-23', 2, 'ORIGINAL',
 '본관 3층 302호', '인터넷 가능한 PC', null,
 null, null, null,
 null, null, null,
 now()),

-- MCQ 재평가
(3, 'MCQ', '2025-06-25', 1, 'RETEST',
 '본관 3층 302호', '인터넷 가능한 PC', null,
 null, null, null,
 null, null, null,
 now()),

-- 루브릭(프로젝트형) 본평가
(4, 'PROJECT', '2024-06-08', 3, 'ORIGINAL',
 '본관 3층 302호', '인터넷 가능한 PC', null,
 '스프링 부트 기반으로 나만의 블로그 시스템을 설계하고 개발하세요.',
 '당신은 스타트업의 백엔드 개발자로 채용되었습니다. 기획자는 블로그 서비스 기능을 요구했고, 디자이너는 아직 없기 때문에 API 문서 기반으로 프론트엔드와 협업해야 합니다. 당신은 다음 요구사항을 구현하고, 문서화 및 팀과 공유해야 합니다.',
 'https://getinthere.notion.site/2178a08b6c0d802087c2fe804ce19b4b?source=copy_link',
 '노션링크',
 'https://getinthere.notion.site/rubric-2128a08b6c0d80898b12f096198cd488?source=copy_link',
 '- 도커로 환경구성\n- 통합테스트',
 now());

-- Question Entity
insert into question_tb(id, paper_id, subject_element_id, no, title, summary, created_at)
values
-- 1번 시험지 문제들
(1, 1, 1, 1, '다음 중 for문 설명으로 틀린것은?',
 '개발자는 반복적인 작업을 처리하기 위해 다양한 반복문을 사용한다. 그 중에서도 for문은 반복 횟수가 명확할 때 자주 활용된다. 다음은 자바에서 for문을 사용하는 전형적인 예이다.', now()),
(2, 1, 2, 2, '다음 중 while문 설명으로 틀린것은?',
 '반복 조건이 불명확할 경우 while문을 사용하는 것이 일반적이다. while문은 조건을 만족하는 동안 코드 블록을 반복 수행하며, break나 continue를 통해 흐름 제어가 가능하다.', now()),

-- 2번 시험지 문제들
(3, 2, 3, 1, '다음 중 select 설명으로 틀린것은?', null, now()),
(4, 2, 4, 2, '다음 중 insert 설명으로 틀린것은?', null, now()),
(5, 2, 5, 3, '다음 중 update 설명으로 틀린것은?', null, now()),
(6, 2, 6, 4, '다음 중 delete 설명으로 틀린것은?', null, now()),
(7, 2, 7, 5, '다음 중 dml 설명으로 틀린것은?', null, now()),

-- 3번 시험지 문제들
(8, 3, 1, 1, '다음 중 스레드 설명으로 틀린것은?', null, now()),
(9, 3, 2, 2, '다음 중 소켓 설명으로 틀린것은?', null, now()),

-- 4번 시험지 문제들 (루브릭 기반)
(10, 4, 8, 1, '백엔드 설계능력',
 '온라인 강의 플랫폼의 강의 등록 기능을 설계하시오.
 - 요구사항: 강의명, 강사명, 가격, 소개글 등록
 - 기술조건: MVC 패턴 기반 설계, 도메인 모델 작성, RESTful API 구조 명세', now()),

(11, 4, 9, 2, 'RestAPI 구현능력',
 '블로그 서비스의 게시글 CRUD API를 구현하시오.
 - 요구사항: 게시글 등록/조회/수정/삭제
 - 기술조건: Spring Boot 기반 REST API, 예외 처리 및 응답 메시지 통일', now()),

(12, 4, 10, 3, '커뮤니케이션능력',
 '팀 프로젝트 회의록을 작성하고, 팀원들과 공유 및 피드백을 주고받는 시나리오를 설명하시오.
 - 요구사항: 회의록 작성 → 피드백 공유 → 수정본 반영
 - 기술조건: 협업툴(Notion, Slack 등) 사용, 피드백 처리 과정 설명 포함', now());


-- QuestionOption Entity
-- question_id 1: '다음 중 for문 설명으로 틀린것은?'
INSERT INTO question_option_tb(id, question_id, no, content, point, created_at)
VALUES (1, 1, 1, '초기화, 조건식, 증감식이 모두 필요하다.', 50, now()), -- 정답 (틀린 것)
       (2, 1, 2, '반복 횟수가 명확할 때 주로 사용된다.', 0, now()),
       (3, 1, 3, '조건식이 참인 동안 반복 블록을 실행한다.', 0, now()),
       (4, 1, 4, '무한 루프를 만들 수 없다.', 0, now());

-- question_id 2: '다음 중 while문 설명으로 틀린것은?'
INSERT INTO question_option_tb(id, question_id, no, content, point, created_at)
VALUES (5, 2, 1, '반드시 초기화, 조건식, 증감식을 함께 명시해야 한다.', 50, now()), -- 정답 (틀린 것)
       (6, 2, 2, '반복 횟수가 불명확할 때 주로 사용된다.', 0, now()),
       (7, 2, 3, '조건식이 거짓이 될 때까지 반복을 수행한다.', 0, now()),
       (8, 2, 4, 'do-while문과 달리 조건 검사를 먼저 수행한다.', 0, now());

-- question_id 3: '다음 중 select 설명으로 틀린것은?'
INSERT INTO question_option_tb(id, question_id, no, content, point, created_at)
VALUES (9, 3, 1, '데이터베이스 테이블의 구조를 변경할 때 사용된다.', 20, now()), -- 정답 (틀린 것, DDL에 해당)
       (10, 3, 2, '테이블에서 데이터를 조회할 때 사용한다.', 0, now()),
       (11, 3, 3, 'WHERE 절을 사용하여 조건을 지정할 수 있다.', 0, now()),
       (12, 3, 4, 'FROM 절에 조회할 테이블을 명시한다.', 0, now());

-- question_id 4: '다음 중 insert 설명으로 틀린것은?'
INSERT INTO question_option_tb(id, question_id, no, content, point, created_at)
VALUES (13, 4, 1, '기존 테이블의 데이터를 수정할 때 사용한다.', 20, now()), -- 정답 (틀린 것, UPDATE에 해당)
       (14, 4, 2, '테이블에 새로운 행을 추가할 때 사용한다.', 0, now()),
       (15, 4, 3, 'VALUES 키워드와 함께 삽입할 값을 명시한다.', 0, now()),
       (16, 4, 4, '컬럼 이름을 생략하면 모든 컬럼에 값을 삽입해야 한다.', 0, now());

-- question_id 5: '다음 중 update 설명으로 틀린것은?'
INSERT INTO question_option_tb(id, question_id, no, content, point, created_at)
VALUES (17, 5, 1, '테이블에서 특정 행을 삭제할 때 사용한다.', 20, now()), -- 정답 (틀린 것, DELETE에 해당)
       (18, 5, 2, '테이블의 기존 데이터를 변경할 때 사용한다.', 0, now()),
       (19, 5, 3, 'SET 절을 사용하여 변경할 컬럼과 값을 지정한다.', 0, now()),
       (20, 5, 4, 'WHERE 절을 생략하면 모든 행이 변경될 수 있다.', 0, now());

-- question_id 6: '다음 중 delete 설명으로 틀린것은?'
INSERT INTO question_option_tb(id, question_id, no, content, point, created_at)
VALUES (21, 6, 1, '테이블의 구조를 삭제할 때 사용한다.', 20, now()), -- 정답 (틀린 것, DROP TABLE에 해당)
       (22, 6, 2, '테이블에서 하나 이상의 행을 삭제할 때 사용한다.', 0, now()),
       (23, 6, 3, 'WHERE 절을 사용하여 삭제할 조건을 지정할 수 있다.', 0, now()),
       (24, 6, 4, 'TRUNCATE와 달리 롤백이 가능하다.', 0, now());

-- question_id 7: '다음 중 dml 설명으로 틀린것은?'
INSERT INTO question_option_tb(id, question_id, no, content, point, created_at)
VALUES (25, 7, 1, '데이터베이스 사용자 권한을 제어하는 명령어를 포함한다.', 20, now()), -- 정답 (틀린 것, DCL에 해당)
       (26, 7, 2, '데이터 조작어(Data Manipulation Language)를 의미한다.', 0, now()),
       (27, 7, 3, 'SELECT, INSERT, UPDATE, DELETE 문이 여기에 속한다.', 0, now()),
       (28, 7, 4, '데이터의 삽입, 수정, 삭제, 조회를 담당한다.', 0, now());

-- question_id 8: '다음 중 스레드 설명으로 틀린것은?'
INSERT INTO question_option_tb(id, question_id, no, content, point, created_at)
VALUES (29, 8, 1, '각 스레드는 독립적인 프로세스 공간을 가진다.', 50, now()), -- 정답 (틀린 것, 프로세스가 독립적인 공간을 가짐)
       (30, 8, 2, '프로세스 내에서 실행되는 실행 단위이다.', 0, now()),
       (31, 8, 3, '같은 프로세스 내의 다른 스레드와 자원을 공유할 수 있다.', 0, now()),
       (32, 8, 4, '멀티태스킹을 구현하는 데 사용될 수 있다.', 0, now());

-- question_id 9: '다음 중 소켓 설명으로 틀린것은?'
INSERT INTO question_option_tb(id, question_id, no, content, point, created_at)
VALUES (33, 9, 1, '네트워크 통신 시 오직 서버 측에서만 생성된다.', 50, now()), -- 정답 (틀린 것, 클라이언트/서버 모두 생성)
       (34, 9, 2, '네트워크 통신을 위한 엔드포인트 역할을 한다.', 0, now()),
       (35, 9, 3, 'IP 주소와 포트 번호의 조합으로 식별된다.', 0, now()),
       (36, 9, 4, '클라이언트와 서버 간의 데이터 송수신에 사용된다.', 0, now());

-- question_id 10
INSERT INTO question_option_tb(id, question_id, no, content, point, created_at)
VALUES (37, 10, 1, '요구사항을 능동적으로 분석하고, 백엔드 전반을 효율적으로 구현하며 테스트 및 문서화까지 완료함', 5, now()),
       (38, 10, 2, '요구사항에 맞춰 백엔드 로직을 구현하고, 테스트까지 수행함', 4, now()),
       (39, 10, 3, '백엔드 기능을 일부 구현하였으나, 예외 처리나 테스트가 부족함', 3, now()),
       (40, 10, 4, '기능 구현은 시도했으나 미완성 상태로 동작하지 않음', 2, now()),
       (41, 10, 5, '기능 구현을 거의 하지 못했거나 시도하지 않음', 1, now());

-- question_id 11
INSERT INTO question_option_tb(id, question_id, no, content, point, created_at)
VALUES (42, 11, 1, '서비스 전반에 걸쳐 RESTful 원칙에 따라 직관적이고 확장 가능한 주소 체계를 완성도 높게 설계함', 5, now()),
       (43, 11, 2, 'RESTful 규칙에 따라 의미 있는 주소 체계를 설계함', 4, now()),
       (44, 11, 3, '기본적인 URL 구성은 했으나 RESTful하지 않거나 일관성이 부족함', 3, now()),
       (45, 11, 4, '주소 체계가 혼란스럽고 기능에 따라 구분이 어려움', 2, now()),
       (46, 11, 5, '주소 설계를 거의 하지 못했거나 의미 없는 주소 사용', 1, now());

-- question_id 12
INSERT INTO question_option_tb(id, question_id, no, content, point, created_at)
VALUES (47, 12, 1, '발음이 명확하고 목소리가 또렷하며, 적절한 손짓과 시선 처리로 청중과 활발히 소통함', 5, now()),
       (48, 12, 2, '손짓, 아이컨택, 또박또박 말함', 4, now()),
       (49, 12, 3, '아이컨택, 제스처만 가능', 3, now()),
       (50, 12, 4, '말하기 불분명 / 시선회피', 2, now()),
       (51, 12, 5, '발표 거부 / 말하지 않음', 1, now());

-- Exam Entity (student_id, teacher_id를 courseStudent_id, courseTeacher_id로 수정)
INSERT INTO exam_tb (id, subject_id, paper_id, course_student_id, course_teacher_id, result_status, not_taken_reason, raw_score,
                     total_score, total_score_percent, grade_level, is_active, copied_paper_version,
                     copied_evaluation_way, copied_max_score, student_sign, student_signed_at, teacher_comment,
                     teacher_commented_at, rubric_submit_link, created_at)
VALUES (1, 1, 1, 1, 1, 'FAIL', NULL, 50.0, 50.0, 50.0, 1, FALSE, 'ORIGINAL', 'MCQ', 100.0,
       null,
        NOW(),
        '미이수입니다. 재평가에 응시하세요.', NOW(), NULL, NOW()),
       (2, 1, 1, 2, 1, 'FAIL', NULL, 50.0, 50.0, 50.0, 1, FALSE, 'ORIGINAL', 'MCQ', 100.0, NULL, NULL,
        '미이수입니다. 재평가에 응시하세요.', NOW(), NULL, NOW()),
       (3, 1, 1, 3, 1, 'PASS', NULL, 100.0, 100.0, 100.0, 5, TRUE, 'ORIGINAL', 'MCQ', 100.0, NULL, NULL,
        '아주 잘했어요. 굳입니다.', NOW(), NULL, NOW()),
       (4, 2, 2, 1, 1, 'PASS', NULL, 80.0, 80.0, 80.0, 4, TRUE, 'ORIGINAL', 'MCQ', 100.0, NULL, NULL, '조금 부족했어요. 굳입니다.',
        NOW(), NULL, NOW()),
       (5, 1, 3, 1, 1, 'PASS', NULL, 90.0, 90.0, 90.0, 5, TRUE, 'RETEST', 'MCQ', 100.0, NULL, NULL,
        '재평가를 잘봤어요. 아주 잘했어요. 굳입니다', NOW(), NULL, NOW()),
       (6, 3, 4, 2, 1, 'NOT_GRADED', NULL, 0.0, 0.0, 0.0, 0, TRUE, 'ORIGINAL', 'RUBRIC', 15.0, NULL, NULL, '', NULL,
        'https://getinthere.notion.site/rubric-2128a08b6c0d80898b12f096198cd488?source=copy_link', NOW());


-- ExamAnswer Entity
INSERT INTO exam_answer_tb (id, exam_id, question_id, question_no, selected_option_no, code_review_request_link,
                            created_at)
VALUES (1, 1, 1, 1, 1, NULL, NOW()),
       (2, 1, 2, 2, 4, NULL, NOW()),
       (3, 2, 1, 1, 4, NULL, NOW()),
       (4, 2, 2, 2, 1, NULL, NOW()),
       (5, 3, 1, 1, 1, NULL, NOW()),
       (6, 3, 2, 2, 1, NULL, NOW()),
       (7, 4, 3, 1, 1, NULL, NOW()),
       (8, 4, 4, 2, 1, NULL, NOW()),
       (9, 4, 5, 3, 1, NULL, NOW()),
       (10, 4, 6, 4, 1, NULL, NOW()),
       (11, 4, 7, 5, 4, NULL, NOW()),
       (12, 5, 8, 1, 1, NULL, NOW()),
       (13, 5, 9, 2, 1, NULL, NOW()),
       -- 채점전이라서 아래 3개는 selected_option_no 가 없다. 이건 채점시에 채워진다.
       (14, 6, 10, 1, NULL,
        'https://github.com/metacoding-books/aws-v2/blob/master/src/main/java/site/metacoding/awsv2/HelloController.java',
        NOW()),
       (15, 6, 11, 2, NULL, NULL, NOW()),
       (16, 6, 12, 3, NULL,
        'https://github.com/metacoding-books/aws-v2/blob/master/src/main/java/site/metacoding/awsv2/HelloController.java',
        NOW());

-- ExamResult Entity
INSERT INTO exam_result_tb (id, exam_answer_id, scored_point, is_correct, code_review_feedback_pr_link, created_at)
VALUES (1, 1, 50.0, TRUE, NULL, NOW()),
       (2, 2, 0.0, FALSE, NULL, NOW()),
       (3, 3, 0.0, FALSE, NULL, NOW()),
       (4, 4, 50.0, TRUE, NULL, NOW()),
       (5, 5, 50.0, TRUE, NULL, NOW()),
       (6, 6, 50.0, TRUE, NULL, NOW()),
       (7, 7, 20.0, TRUE, NULL, NOW()),
       (8, 8, 20.0, TRUE, NULL, NOW()),
       (9, 9, 20.0, TRUE, NULL, NOW()),
       (10, 10, 20.0, TRUE, NULL, NOW()),
       (11, 11, 0.0, FALSE, NULL, NOW()),
       (12, 12, 50.0, TRUE, NULL, NOW()),
       (13, 13, 50.0, TRUE, NULL, NOW()),
       (14, 14, 0.0, FALSE,
        'https://github.com/metacoding-books/aws-v2/blob/master/src/main/java/site/metacoding/awsv2/HelloController.java',
        NOW()),
       (15, 15, 0.0, FALSE, NULL, NOW()),
       (16, 16, 0.0, FALSE,
        'https://github.com/metacoding-books/aws-v2/blob/master/src/main/java/site.metacoding.awsv2/HelloController.java',
        NOW());