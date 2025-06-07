package shop.mtcoding.blog.web.exam;

import shop.mtcoding.blog.domain.course.exam.Exam;
import shop.mtcoding.blog.domain.course.subject.element.SubjectElement;
import shop.mtcoding.blog.domain.user.teacher.Teacher;

import java.util.List;

public class ExamModel {
    public record ResultDetail(
            Exam exam,
            List<SubjectElement> subjectElements,
            Teacher teacher,
            Long prevExamId,
            Long nextExamId,
            int currentIndex,
            Long originExamId
    ) {
    }
}
