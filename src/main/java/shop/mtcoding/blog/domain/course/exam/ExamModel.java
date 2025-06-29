package shop.mtcoding.blog.domain.course.exam;

import shop.mtcoding.blog.domain.course.subject.Subject;
import shop.mtcoding.blog.domain.course.subject.element.SubjectElement;
import shop.mtcoding.blog.domain.course.subject.paper.EvaluationWay;
import shop.mtcoding.blog.domain.course.subject.paper.Paper;
import shop.mtcoding.blog.domain.course.subject.paper.question.Question;
import shop.mtcoding.blog.domain.user.student.Student;
import shop.mtcoding.blog.domain.user.teacher.Teacher;

import java.util.List;
import java.util.Map;

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

    public record PaperItems(Long studentId, List<Paper> papers, Map<Long, Boolean> attendanceMap) {
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
