package shop.mtcoding.blog.exam.web.dto;

import shop.mtcoding.blog.exam.application.domain.enums.ExamNotTakenReason;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 시험 채점 및 미응시 사유 등록을 위한 요청 DTO 클래스.
 * 모든 내부 클래스가 불변(immutable) 객체인 record로 재구성되었습니다.
 */
public class ExamTeacherRequest {

    /**
     * 객관식 시험 채점 요청 레코드.
     * DTO 역할을 하며, ExamTeacherCommand.GradeMcq 레코드로 변환하는 기능을 포함합니다.
     */
    public record GradeMcq(
            Long examId,
            String teacherComment,
            List<AnswerMcq> answers) {
        /**
         * 객관식 시험 답안지 내의 개별 답안 레코드.
         */
        public record AnswerMcq(
                Integer answerId,
                Integer selectedOptionNo) {
        }

    }

    /**
     * 주관식 시험 채점 요청 레코드.
     * DTO 역할을 하며, ExamTeacherCommand.GradeRubric 레코드로 변환하는 기능을 포함합니다.
     */
    public record GradeRubric(
            Integer examId,
            String teacherComment,
            List<AnswerRubric> answers) {
        /**
         * 주관식 시험 답안지 내의 개별 답안 레코드.
         */
        public record AnswerRubric(
                Long answerId,
                Integer selectedOptionNo,
                String codeReviewFeedbackPRLink) {
        }

    }

    /**
     * 미응시 사유 등록 요청 레코드.
     * DTO 역할을 하며, ExamTeacherCommand.NotTakenReason 레코드로 변환하는 기능을 포함합니다.
     */
    public record NotTakenReason(
            Long studentId,
            Long paperId,
            ExamNotTakenReason notTakenReason) {

    }
}
