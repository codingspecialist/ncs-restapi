package shop.mtcoding.blog.exam.web.dto;

import lombok.Data;
import shop.mtcoding.blog._core.errors.exception.api.Exception400;
import shop.mtcoding.blog._core.errors.exception.api.Exception404;
import shop.mtcoding.blog.course.application.domain.CourseStudent;
import shop.mtcoding.blog.exam.application.domain.Exam;
import shop.mtcoding.blog.exam.application.domain.ExamAnswer;
import shop.mtcoding.blog.exam.application.domain.Paper;
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

        public Exam toEntityWithAnswers(CourseStudent courseStudent, Paper paper, List<Question> questionList) {
            Exam exam = Exam.createRubricExam(courseStudent, paper, rubricSubmitLink);
            for (Answer answer : answers) {
                Question question = questionList.stream()
                        .filter(q -> q.getNo().equals(answer.getQuestionNo()))
                        .findFirst()
                        .orElseThrow(() -> new Exception404("해당 questionNo 없음: " + answer.getQuestionNo()));
                exam.addAnswer(answer.toEntity(exam, question));
            }
            return exam;
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

        public Exam toEntityWithAnswers(CourseStudent courseStudent, Paper paper, List<Question> questionList) {
            Exam exam = Exam.createMcqExam(courseStudent, paper);
            for (AnswerDTO dto : answers) {
                Question question = questionList.stream()
                        .filter(q -> q.getNo().equals(dto.getQuestionNo()))
                        .findFirst()
                        .orElseThrow(() -> new Exception404("해당 questionNo 없음: " + dto.getQuestionNo()));
                exam.addAnswer(dto.toEntity(exam, question));
            }
            return exam;
        }
    }

    // ✅ 서명 제출
    @Data
    public static class SignDTO {
        private Long examId;
        private String sign;
    }
}
