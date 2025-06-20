package shop.mtcoding.blog.web.exam;

import lombok.Data;
import shop.mtcoding.blog.core.errors.exception.api.ApiException400;
import shop.mtcoding.blog.domain.course.exam.answer.ExamAnswer;
import shop.mtcoding.blog.domain.course.subject.paper.question.Question;
import shop.mtcoding.blog.domain.course.subject.paper.question.option.QuestionOption;

import java.util.List;

public class ExamRequest {


    @Data
    public static class UpdateDTO {
        private String teacherComment;
        private Boolean standby;
        private List<AnswerDTO> answers;

        @Data
        public static class AnswerDTO {
            private Integer answerId;
            private Integer selectedOptionNo; // 정답 번호 (PK 아님)
            private String codeReviewPRLink;

            public void update(Question question, ExamAnswer answer) {
                if (selectedOptionNo == null) {
                    throw new ApiException400("모든 문제에 대한 답안을 제출해야 됩니다");
                }

                boolean isRight = question.getQuestionOptions().stream()
                        .filter(QuestionOption::getIsRight) // 정답 후보들만 필터링
                        .anyMatch(option -> option.getNo().equals(selectedOptionNo)); // 수험생의 선택과 일치하는지

                String finalReviewLink = (codeReviewPRLink != null && codeReviewPRLink.trim().isEmpty()) ? null : codeReviewPRLink;
                answer.update(selectedOptionNo, isRight, finalReviewLink);

                answer.autoGrade();
            }

        }
    }

    @Data
    public static class AbsentDTO {
        private Long studentId;
        private Long paperId;
    }

}
