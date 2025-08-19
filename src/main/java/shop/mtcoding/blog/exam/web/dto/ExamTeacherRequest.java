package shop.mtcoding.blog.exam.web.dto;

import shop.mtcoding.blog.exam.application.domain.enums.ExamNotTakenReason;
import shop.mtcoding.blog.exam.application.service.dto.ExamTeacherCommand;

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
            List<AnswerMcq> answers
    ) {
        /**
         * 객관식 시험 답안지 내의 개별 답안 레코드.
         */
        public record AnswerMcq(
                Integer answerId,
                Integer selectedOptionNo
        ) {
        }

        /**
         * 이 GradeMcq 레코드를 ExamTeacherCommand.GradeMcq 커맨드로 변환합니다.
         *
         * @return 변환된 ExamTeacherCommand.GradeMcq 레코드 객체
         */
        public ExamTeacherCommand.GradeMcq toCommand() {
            // answers 리스트를 ExamTeacherCommand의 내부 DTO 리스트로 변환
            List<ExamTeacherCommand.GradeMcq.AnswerMcq> mcqAnswers = this.answers.stream()
                    .map(answer -> new ExamTeacherCommand.GradeMcq.AnswerMcq(answer.answerId(), answer.selectedOptionNo()))
                    .collect(Collectors.toList());

            // 변환된 데이터를 사용하여 ExamTeacherCommand.GradeMcq 레코드 객체 생성
            return new ExamTeacherCommand.GradeMcq(this.examId, this.teacherComment, mcqAnswers);
        }
    }

    /**
     * 주관식 시험 채점 요청 레코드.
     * DTO 역할을 하며, ExamTeacherCommand.GradeRubric 레코드로 변환하는 기능을 포함합니다.
     */
    public record GradeRubric(
            Integer examId,
            String teacherComment,
            List<AnswerRubric> answers
    ) {
        /**
         * 주관식 시험 답안지 내의 개별 답안 레코드.
         */
        public record AnswerRubric(
                Long answerId,
                Integer selectedOptionNo,
                String codeReviewFeedbackPRLink
        ) {
        }

        /**
         * 이 GradeRubric 레코드를 ExamTeacherCommand.GradeRubric 커맨드로 변환합니다.
         *
         * @return 변환된 ExamTeacherCommand.GradeRubric 레코드 객체
         */
        public ExamTeacherCommand.GradeRubric toCommand() {
            // answers 리스트를 ExamTeacherCommand의 내부 DTO 리스트로 변환
            List<ExamTeacherCommand.GradeRubric.AnswerRubric> rubricAnswers = this.answers.stream()
                    .map(answer -> new ExamTeacherCommand.GradeRubric.AnswerRubric(answer.answerId(), answer.selectedOptionNo(), answer.codeReviewFeedbackPRLink()))
                    .collect(Collectors.toList());

            // 변환된 데이터를 사용하여 ExamTeacherCommand.GradeRubric 레코드 객체 생성
            return new ExamTeacherCommand.GradeRubric(this.examId, this.teacherComment, rubricAnswers);
        }
    }

    /**
     * 미응시 사유 등록 요청 레코드.
     * DTO 역할을 하며, ExamTeacherCommand.NotTakenReason 레코드로 변환하는 기능을 포함합니다.
     */
    public record NotTakenReason(
            Long studentId,
            Long paperId,
            ExamNotTakenReason notTakenReason
    ) {
        /**
         * 이 NotTakenReason 레코드를 ExamTeacherCommand.NotTakenReason 커맨드로 변환합니다.
         *
         * @return 변환된 ExamTeacherCommand.NotTakenReason 레코드 객체
         */
        public ExamTeacherCommand.NotTakenReason toCommand() {
            // ExamTeacherCommand.NotTakenReason 레코드 객체 반환
            return new ExamTeacherCommand.NotTakenReason(this.studentId, this.paperId, this.notTakenReason);
        }
    }
}
