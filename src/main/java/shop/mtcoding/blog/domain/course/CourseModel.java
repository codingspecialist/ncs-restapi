package shop.mtcoding.blog.domain.course;

import org.springframework.data.domain.Page;
import shop.mtcoding.blog.domain.course.student.Student;
import shop.mtcoding.blog.domain.course.subject.Subject;

import java.util.List;

public class CourseModel {
    public record Detail(Course course, List<Subject> subjects, List<Student> students) {
    }

    public record Items(Page<Course> coursePG) {
    }

}
