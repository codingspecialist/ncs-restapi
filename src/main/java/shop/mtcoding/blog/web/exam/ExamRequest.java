package shop.mtcoding.blog.web.exam;

import lombok.Data;

import java.util.List;

public class ExamRequest {

    // 강사가 총평 작성
    @Data
    public static class GradeDTO {
        private String teacherComment;
        private List<AnswerGradeDTO> answerGrades;

        @Data
        class AnswerGradeDTO {
            private Integer answerId;
            private Integer selectedOptionNo;
            private String codeReviewPRLink; // rubric 일때만 받음
        }
    }

    @Data
    public static class AbsentDTO {
        private Long studentId;
        private Long paperId;
    }

    @Data
    public static class NotTakenDTO {
        private Long studentId;
        private Long paperId;
    }

}
