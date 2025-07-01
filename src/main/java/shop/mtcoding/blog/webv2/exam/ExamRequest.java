package shop.mtcoding.blog.webv2.exam;

import lombok.Data;
import shop.mtcoding.blog.domainv2222222.course.exam.ExamNotTakenReason;

import java.util.List;

public class ExamRequest {

    @Data
    public static class GradeMcq {
        private Long examId;
        private String teacherComment; // exam
        private List<AnswerMcq> answers;

        @Data
        public static class AnswerMcq {
            private Integer answerId;
            private Integer selectedOptionNo; // examAnswer
        }
    }

    @Data
    public static class GradeRubric {
        private Integer examId;
        private String teacherComment; // exam
        private List<AnswerRubric> answers;

        @Data
        public static class AnswerRubric {
            private Long answerId;
            private Integer selectedOptionNo; // examAnswer
            private String codeReviewFeedbackPRLink; // examResult
        }
    }

    @Data
    public static class NotTakenReason {
        private Long studentId;
        private Long paperId;
        private ExamNotTakenReason notTakenReason;
    }

}
