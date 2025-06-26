package shop.mtcoding.blog.web.student.exam;

import lombok.Data;
import shop.mtcoding.blog.core.errors.exception.api.Exception400;
import shop.mtcoding.blog.domain.course.exam.Exam;
import shop.mtcoding.blog.domain.course.exam.answer.ExamAnswer;
import shop.mtcoding.blog.domain.course.subject.paper.Paper;
import shop.mtcoding.blog.domain.course.subject.paper.question.Question;
import shop.mtcoding.blog.domain.user.student.Student;

import java.util.List;

public class StudentExamRequest {

    // ✅ 루브릭 시험 응시
    @Data
    public static class RubricSaveDTO {
        private Long paperId;
        private String rubricSubmitLink;
        private List<AnswerDTO> answers;

        @Data
        public static class AnswerDTO {
            private Integer questionNo;
            private Integer selectedOptionNo;
            private String codeReviewRequestLink;

            public ExamAnswer toEntity(Question question, Exam exam) {
                return ExamAnswer.builder()
                        .exam(exam)
                        .question(question)
                        .questionNo(questionNo)
                        .selectedOptionNo(selectedOptionNo)
                        .codeReviewRequestLink(codeReviewRequestLink)
                        .build();
            }
        }

        // 시험 객체 생성
        public Exam toEntity(Student student, Paper paper) {
            return Exam.createRubricExamIsNotGraded(student, paper, rubricSubmitLink);
        }
    }

    // ✅ 객관식 시험 응시
    @Data
    public static class McqSaveDTO {
        private Long paperId;
        private List<AnswerDTO> answers;

        @Data
        public static class AnswerDTO {
            private Integer questionNo;
            private Integer selectedOptionNo;

            public ExamAnswer toEntity(Question question, Exam exam) {
                if (selectedOptionNo == null) {
                    throw new Exception400("모든 문제에 대한 답안을 제출해야 됩니다");
                }

                return ExamAnswer.builder()
                        .exam(exam)
                        .question(question)
                        .questionNo(questionNo)
                        .selectedOptionNo(selectedOptionNo)
                        .build();
            }
        }

        public Exam toEntity(Student student, Paper paper) {
            return Exam.createMcqExam(student, paper);
        }
    }

    // ✅ 서명 제출
    @Data
    public static class SignDTO {
        private Long examId;
        private String sign;
    }
}
