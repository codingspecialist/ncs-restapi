package shop.mtcoding.blog.exam.application.service.dto;

import shop.mtcoding.blog.exam.application.domain.enums.ExamNotTakenReason;

import java.util.List;

public class ExamTeacherCommand {
    // 객관식 시험 채점을 위한 레코드
    public record GradeMcq(
            Long examId,
            String teacherComment,
            List<AnswerMcq> answers
    ) {
        public record AnswerMcq(
                Integer answerId,
                Integer selectedOptionNo
        ) {
        }
    }

    // 주관식 시험 채점을 위한 레코드
    public record GradeRubric(
            Integer examId,
            String teacherComment,
            List<AnswerRubric> answers
    ) {
        public record AnswerRubric(
                Long answerId,
                Integer selectedOptionNo,
                String codeReviewFeedbackPRLink
        ) {
        }
    }

    // 미응시 사유 등록을 위한 레코드
    public record NotTakenReason(
            Long studentId,
            Long paperId,
            ExamNotTakenReason notTakenReason
    ) {
    }
}
