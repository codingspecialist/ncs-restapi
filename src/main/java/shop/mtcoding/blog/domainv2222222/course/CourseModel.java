package shop.mtcoding.blog.domainv2222222.course;

import org.springframework.data.domain.Page;
import shop.mtcoding.blog.domainv2222222.course.subject.Subject;
import shop.mtcoding.blog.domainv2222222.user.student.Student;

import java.util.List;

public class CourseModel {
    public record Item(Course course) {
    }

    public record Detail(Course course, List<Subject> subjects, List<Student> students) {
    }

    public record Slice(Page<Course> coursePG) {
    }

}
