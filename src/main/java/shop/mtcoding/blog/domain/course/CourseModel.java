package shop.mtcoding.blog.domain.course;

import org.springframework.data.domain.Page;
import shop.mtcoding.blog.domain.course.subject.Subject;
import shop.mtcoding.blog.domain.user.student.Student;

import java.util.List;

public class CourseModel {
    public record Item(Course course) {
    }

    public record Detail(Course course, List<Subject> subjects, List<Student> students) {
    }

    public record Slice(Page<Course> coursePG) {
    }

}
