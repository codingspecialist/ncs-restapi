### 3. URL 설계 변경

| 부분        | 의미                 | 설명                       |
|-----------|--------------------|--------------------------|
| `teacher` | **역할 (Role)**      | 요청 주체: 선생님 (로그인 유저)      |
| `exam`    | **기능 (Purpose)**   | 이 흐름이 "시험 평가" 기능에 속함을 암시 |
| `courses` | **도메인 (Resource)** | 실제로 조회하는 대상은 "과정 목록"     |

| 케이스                    | URL 형태                        | 이유                                                 |
|------------------------|-------------------------------|----------------------------------------------------|
| **목록 조회 / 계층적 탐색**     | `/courses/1/subjects`         | 상위 리소스에 종속된 하위 목록 (context 필요)                     |
| **상세 조회 (식별자 충분)**     | `/subjects/5`                 | subjectId로 고유 식별 가능 (상위 경로 필요 없음)                  |
| **정합성 검증이 필요한 상세 조회**  | `/courses/1/subjects/5`       | subjectId가 courseId에 포함되는지 확인 목적                   |
| **상위 리소스를 유추 불가능한 경우** | `/users/1/posts/5/comments/7` | commentId만으로는 상위 유추 불가능 → 전체 경로 필요                 |
| **시험 결과 상세**           | `/exams/3/result`             | examId 하나로 course, subject, student 유추 가능 → 단순화 가능 |

```text
GET     /admin/courses                    → 전체 과정 목록
POST    /admin/courses                    → 과정 등록
GET     /admin/courses/{courseId}         → 과정 상세
PUT     /admin/courses/{courseId}         → 과정 수정
DELETE  /admin/courses/{courseId}         → 과정 삭제

GET     /teacher/admin-courses                     → 내가 등록한 과정 목록
POST    /teacher/admin-courses                     → 내가 등록한 과정 생성
GET     /teacher/admin-courses/{courseId}/subjects → 과정의 교과목 목록

GET     /teacher/exam-courses                     → 내가 평가할 과정 목록
GET     /teacher/exam-courses/{courseId}/subjects → 평가용 과정의 교과목 목록
GET     /teacher/subjects/{subjectId}/exams       → 교과목의 시험 목록
GET     /teacher/exams/{examId}/result            → 시험 결과 상세 (examId로 충분)

GET     /student/exams                   → 내가 응시한 시험 목록
GET     /student/exams/{examId}/result   → 시험 결과 보기
POST    /student/exams/{examId}/submit   → 시험 응시 제출

```

| 역할  | 기능       | URL 예시                                  |
|-----|----------|-----------------------------------------|
| 관리자 | 과정 전체 관리 | `/admin/courses`, `/admin/courses/{id}` |
| 선생님 | 과정 평가 흐름 | `/teacher/exam-courses`                 |
| 선생님 | 교과목 → 시험 | `/teacher/subjects/{subjectId}/exams`   |
| 선생님 | 시험 결과 상세 | `/teacher/exams/{examId}/result`        |
| 학생  | 내 시험 목록  | `/student/exams`                        |
| 학생  | 내 시험 결과  | `/student/exams/{examId}/result`        |
