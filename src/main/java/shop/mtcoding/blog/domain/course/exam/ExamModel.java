package shop.mtcoding.blog.domain.course.exam;

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
}
