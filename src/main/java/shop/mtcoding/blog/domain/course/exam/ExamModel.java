package shop.mtcoding.blog.domain.course.exam;

import shop.mtcoding.blog.domain.course.student.StudentStatus;
import shop.mtcoding.blog.domain.course.subject.element.SubjectElement;
import shop.mtcoding.blog.domain.course.subject.paper.Paper;
import shop.mtcoding.blog.domain.course.subject.paper.question.Question;
import shop.mtcoding.blog.domain.user.teacher.Teacher;

import java.util.List;
import java.util.Map;

public class ExamModel {
    public record ResultDetail(Exam exam, List<SubjectElement> subjectElements, Teacher teacher, Long prevExamId,
                               Long nextExamId, int currentIndex, Long originExamId) {
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
            String examState,
            String teacherName,
            Double score,
            Integer grade,
            String passState,
            String reExamReason,
            Long studentId,
            Long paperId,
            StudentStatus studentStatus,
            Boolean isAbsent,
            Boolean gradingComplete
    ) {
    }
}
