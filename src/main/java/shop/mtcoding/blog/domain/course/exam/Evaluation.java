package shop.mtcoding.blog.domain.course.exam;

//| 필드명            | 타입       | 설명                           |
//        | -------------- | -------- | ---------------------------- |
//        | `id`           | PK       | 평가 고유 번호                     |
//        | `student_id`   | FK       | 평가받는 학생                      |
//        | `question_id`  | FK       | 평가 항목 (발표력 등)                |
//        | `option_id`    | FK       | 선택된 루브릭 옵션 (1\~5점 중 선택된 것)   |
//        | `score`        | int      | 실제 점수 (optionPoint로부터 유도 가능) |
//        | `comment`      | text     | 평가자가 남긴 코멘트                  |
//        | `file_url`     | string   | 발표자료 업로드 파일 경로               |
//        | `evaluator_id` | FK       | 평가자 (멘토)                     |
//        | `created_at`   | datetime | 평가 시간                        |
// 서술형, 작업형, 프로젝트평가 저장 테이블
public class Evaluation {
}
