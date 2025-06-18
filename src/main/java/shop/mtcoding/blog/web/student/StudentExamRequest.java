package shop.mtcoding.blog.web.student;

import lombok.Data;
import shop.mtcoding.blog.core.errors.exception.api.ApiException400;
import shop.mtcoding.blog.domain.course.exam.Exam;
import shop.mtcoding.blog.domain.course.exam.answer.ExamAnswer;
import shop.mtcoding.blog.domain.course.student.Student;
import shop.mtcoding.blog.domain.course.subject.paper.Paper;
import shop.mtcoding.blog.domain.course.subject.paper.question.Question;
import shop.mtcoding.blog.domain.course.subject.paper.question.option.QuestionOption;

import java.util.List;

public class StudentExamRequest {

    @Data
    public static class RubricSaveDTO {
        private Long paperId;
        private String teacherName;
        private String submitLink;
        private List<AnswerDTO> answers;

        @Data
        public static class AnswerDTO {
            private Integer questionNo;
            private String codeReviewLink;

            public ExamAnswer toEntity(Question question, Exam exam) {

                // ExamAnswer 생성
                return ExamAnswer.builder()
                        .exam(exam)
                        .question(question)
                        .questionNo(questionNo)
                        .codeReviewLink(codeReviewLink) // nullable
                        .earnedPoint(0) // 수동 채점 시 업데이트
                        .isRight(false)  // 수동 채점이므로 false
                        .build();
            }
        }

        // Exam 생성 (시험 응시 결과)
        public Exam toEntity(Paper paper, Student student, String passState, Double score, Integer grade, String reExamReason) {
            return Exam.builder()
                    .isUse(true)
                    .paper(paper)
                    .subject(paper.getSubject())
                    .student(student)
                    .teacherName(teacherName)
                    .passState(passState)
                    .score(score)
                    .grade(grade)
                    .examState(paper.getPaperType().toKorean())
                    .reExamReason(reExamReason)
                    .standby(false)
                    .finalScore(0.0)
                    .build();
        }
    }


    @Data
    public static class McqSaveDTO {
        private Long paperId;
        private String teacherName;
        private List<AnswerDTO> answers;

        @Data
        public static class AnswerDTO {
            private Integer questionNo;         // 문제 번호 (PK 아님)
            private Integer selectedOptionNo;   // 선택한 보기 번호 (PK 아님)

            // 채점 및 엔티티 변환
            public ExamAnswer toEntity(Question question, Exam exam) {
                if (selectedOptionNo == null) {
                    throw new ApiException400("모든 문제에 대한 답안을 제출해야 됩니다");
                }

                // 선택한 보기 찾기
                QuestionOption selectedOption = question.getQuestionOptions().stream()
                        .filter(opt -> opt.getNo().equals(selectedOptionNo))
                        .findFirst()
                        .orElseThrow(() -> new ApiException400("해당 보기 번호가 존재하지 않습니다"));

                // earnedPoint: 정답일 경우 해당 보기에 설정된 점수, 오답일 경우 0점
                int earnedPoint = selectedOption.getIsRight() ? selectedOption.getPoint() : 0;

                // ExamAnswer 생성
                return ExamAnswer.builder()
                        .exam(exam)
                        .question(question)
                        .questionNo(questionNo)
                        .selectedOptionNo(selectedOptionNo)
                        .isRight(selectedOption.getIsRight())
                        .earnedPoint(earnedPoint)
                        .build();
            }
        }

        // Exam 생성 (시험 응시 결과)
        public Exam toEntity(Paper paper, Student student, String passState, Double score, Integer grade, String reExamReason) {
            return Exam.builder()
                    .isUse(true)
                    .paper(paper)
                    .subject(paper.getSubject())
                    .student(student)
                    .teacherName(teacherName)
                    .passState(passState)
                    .score(score)
                    .grade(grade)
                    .examState(paper.getPaperType().toKorean())
                    .reExamReason(reExamReason)
                    .standby(true)
                    .finalScore(0.0)
                    .build();
        }
    }


    @Data
    public static class SignDTO {
        private Long examId;
        private String sign;
    }
}
