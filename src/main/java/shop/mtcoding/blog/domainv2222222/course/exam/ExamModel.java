package shop.mtcoding.blog.domainv2222222.course.exam;

import shop.mtcoding.blog.domainv2222222.course.subject.Subject;
import shop.mtcoding.blog.domainv2222222.course.subject.element.SubjectElement;
import shop.mtcoding.blog.domainv2222222.course.subject.paper.EvaluationWay;
import shop.mtcoding.blog.domainv2222222.course.subject.paper.Paper;
import shop.mtcoding.blog.domainv2222222.course.subject.paper.question.Question;
import shop.mtcoding.blog.domainv2222222.user.student.Student;
import shop.mtcoding.blog.domainv2222222.user.teacher.Teacher;

import java.util.List;

public class ExamModel {
    public record ResultDetails(EvaluationWay evaluationWay, List<Exam> exams, List<SubjectElement> subjectElements,
                                Teacher teacher) {
    }

    public record ResultDetail(EvaluationWay evaluationWay, Exam exam, List<SubjectElement> subjectElements,
                               Teacher teacher) {
    }

    public record Start(Paper paperPS, String studentName, List<SubjectElement> subjectElementListPS,
                        List<Question> questionListPS) {
    }

    public record PaperItems(Long studentId, List<PaperItem> papers) {
    }

    /**
     * 학생에게 보여줄 시험지 목록의 각 항목
     *
     * @param paper  원본 시험지 객체
     * @param status 학생의 현재 응시 상태
     */
    public record PaperItem(Paper paper, ExamTakingStatus status) {
    }

    public record ExamItems(List<Exam> exams) {
    }

    public record Result(
            Long examId,
            String studentName,
            String subjectTitle,
            String teacherName,
            Double totalScorePercent,
            Integer gradeLevel,
            String resultStatus,
            String notTakenReason,
            Long studentId,
            Long paperId,
            Boolean isActive,
            String studentStatus
    ) {
        public static Result fromExam(Exam exam) {
            return new Result(
                    exam.getId(),
                    exam.getStudent().getName(),
                    exam.getSubject().getTitle(),
                    exam.getTeacher().getName(),
                    exam.getTotalScorePercent(),
                    exam.getGradeLevel(),
                    exam.getResultStatus().toKorean(),
                    exam.getNotTakenReason() != null ? exam.getNotTakenReason().toKorean() : "",
                    exam.getStudent().getId(),
                    exam.getPaper().getId(),
                    exam.getIsActive(),
                    exam.getStudent().getStudentStatus().toKorean()
            );
        }

        public static Result createNotTakenTemplate(Student student, Subject subject, Paper paper) {
            return new Result(
                    null,
                    student.getName(),
                    subject.getTitle(),
                    subject.getTeacher().getName(),
                    0.0,
                    1,
                    ExamResultStatus.NOT_TAKEN.toKorean(),
                    "",
                    student.getId(),
                    paper.getId(),
                    true,
                    student.getStudentStatus().toKorean()
            );
        }
    }
}
