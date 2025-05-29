package shop.mtcoding.blog.domain.course;

import shop.mtcoding.blog.domain.course.student.Student;
import shop.mtcoding.blog.domain.course.subject.Subject;

import java.util.List;

public class CourseFlow {
    public record Detail(Course course, List<Subject> subjects, List<Student> students) {
    }


}
