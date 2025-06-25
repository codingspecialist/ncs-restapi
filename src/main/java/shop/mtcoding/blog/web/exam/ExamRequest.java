package shop.mtcoding.blog.web.exam;

import lombok.Data;

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
        }
    }

    @Data
    public static class AbsentDTO {
        private Long studentId;
        private Long paperId;
    }

}
