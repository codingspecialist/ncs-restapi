package shop.mtcoding.blog.web.exam;

import lombok.Data;
import shop.mtcoding.blog.core.errors.exception.api.ApiException400;
import shop.mtcoding.blog.domain.course.exam.answer.ExamAnswer;
import shop.mtcoding.blog.domain.course.subject.paper.question.Question;

import java.util.List;

public class ExamRequest {

    @Data
    public static class UpdateDTO {
        private String teacherComment;
        private List<AnswerDTO> answers;

        @Data
        public static class AnswerDTO {
            private Integer answerId;
            private Integer selectedOptionNo; // 정답 번호 (PK 아님)

            // 통과헀으면, 이유 삭제해야함
            public void update(Question question, ExamAnswer answer) {
                if (selectedOptionNo == null) throw new ApiException400("모든 문제에 대한 답안을 제출해야 됩니다");

                boolean isCollect = true;
/*                if (question.getAnswerNumber().equals(selectedOptionNo)) {
                    isCollect = true;
                } else {
                    isCollect = false;
                }*/

                answer.update(selectedOptionNo, isCollect);
            }

        }
    }

    @Data
    public static class AbsentDTO {
        private Long studentId;
        private Long paperId;
    }

}
