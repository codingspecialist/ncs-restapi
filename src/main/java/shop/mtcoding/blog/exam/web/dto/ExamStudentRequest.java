package shop.mtcoding.blog.exam.web.dto;

import lombok.Data;
import shop.mtcoding.blog._core.errors.exception.api.Exception400;
import shop.mtcoding.blog.exam.application.domain.Exam;
import shop.mtcoding.blog.exam.application.domain.ExamAnswer;
import shop.mtcoding.blog.exam.application.domain.Question;

import java.util.List;

public class ExamStudentRequest {

    @Data
    public static class RubricSave {
        private Long paperId;
        private String rubricSubmitLink;
        private List<Answer> answers;

        @Data
        public static class Answer {
            private Integer questionNo;
            private String codeReviewRequestLink;

            public ExamAnswer toEntity(Exam exam, Question question) {
                return ExamAnswer.createRubricAnswer(exam, question, questionNo, codeReviewRequestLink);
            }
        }

    }

    // ✅ 객관식 시험 응시
    @Data
    public static class McqSave {
        private Long paperId;
        private List<AnswerDTO> answers;

        @Data
        public static class AnswerDTO {
            private Integer questionNo;
            private Integer selectedOptionNo;

            public ExamAnswer toEntity(Exam exam, Question question) {
                if (selectedOptionNo == null) {
                    throw new Exception400("모든 문제에 대한 답안을 제출해야 됩니다");
                }
                return ExamAnswer.createMcqAnswer(exam, question, questionNo, selectedOptionNo);
            }
        }


    }

    // ✅ 서명 제출
    @Data
    public static class SignDTO {
        private Long examId;
        private String sign;
    }
}
