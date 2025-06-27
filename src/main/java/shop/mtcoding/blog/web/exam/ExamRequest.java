package shop.mtcoding.blog.web.exam;

import lombok.Data;

public class ExamRequest {

    // 강사가 총평 작성
    @Data
    public static class GradeDTO {
        private Integer answerId;
        private Integer selectedOptionNo;
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
