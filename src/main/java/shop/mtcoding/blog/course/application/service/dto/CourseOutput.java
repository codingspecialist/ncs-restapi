package shop.mtcoding.blog.course.application.service.dto;

import org.springframework.data.domain.Page;
import shop.mtcoding.blog.course.application.domain.Course;
import shop.mtcoding.blog.course.application.domain.CourseStudent;
import shop.mtcoding.blog.course.application.domain.Subject;

import java.util.List;

public class CourseOutput {
    public record MaxPage(Page<Course> coursePG) {
    }

    public record Max(Course course) {
    }

    public record Detail(Course course, List<Subject> subjects, List<CourseStudent> students) {
    }
}
 