package shop.mtcoding.blog.exam.web.dto;

import lombok.Data;
import shop.mtcoding.blog.exam.application.domain.enums.ExamNotTakenReason;
import shop.mtcoding.blog.exam.application.service.dto.ExamTeacherCommand;

import java.util.List;
import java.util.stream.Collectors;

public class ExamTeacherRequest {

    @Data
    public static class GradeMcq {
        private Long examId;
        private String teacherComment;
        private List<AnswerMcq> answers;

        @Data
        public static class AnswerMcq {
            private Integer answerId;
            private Integer selectedOptionNo;
        }

        // GradeMcq DTO를 ExamTeacherCommand.GradeMcq 레코드로 변환
        public ExamTeacherCommand.GradeMcq toCommand() {
            List<ExamTeacherCommand.GradeMcq.AnswerMcq> mcqAnswers = this.answers.stream()
                    .map(answer -> new ExamTeacherCommand.GradeMcq.AnswerMcq(answer.getAnswerId(), answer.getSelectedOptionNo()))
                    .collect(Collectors.toList());

            // ExamTeacherCommand.GradeMcq 레코드 객체 반환
            return new ExamTeacherCommand.GradeMcq(this.examId, this.teacherComment, mcqAnswers);
        }
    }

    @Data
    public static class GradeRubric {
        private Integer examId;
        private String teacherComment;
        private List<AnswerRubric> answers;

        @Data
        public static class AnswerRubric {
            private Long answerId;
            private Integer selectedOptionNo;
            private String codeReviewFeedbackPRLink;
        }

        // GradeRubric DTO를 ExamTeacherCommand.GradeRubric 레코드로 변환
        public ExamTeacherCommand.GradeRubric toCommand() {
            List<ExamTeacherCommand.GradeRubric.AnswerRubric> rubricAnswers = this.answers.stream()
                    .map(answer -> new ExamTeacherCommand.GradeRubric.AnswerRubric(answer.getAnswerId(), answer.getSelectedOptionNo(), answer.getCodeReviewFeedbackPRLink()))
                    .collect(Collectors.toList());

            // ExamTeacherCommand.GradeRubric 레코드 객체 반환
            return new ExamTeacherCommand.GradeRubric(this.examId, this.teacherComment, rubricAnswers);
        }
    }

    @Data
    public static class NotTakenReason {
        private Long studentId;
        private Long paperId;
        private ExamNotTakenReason notTakenReason;

        // NotTakenReason DTO를 ExamTeacherCommand.NotTakenReason 레코드로 변환
        public ExamTeacherCommand.NotTakenReason toCommand() {
            // ExamTeacherCommand.NotTakenReason 레코드 객체 반환
            return new ExamTeacherCommand.NotTakenReason(this.studentId, this.paperId, this.notTakenReason);
        }
    }
}
