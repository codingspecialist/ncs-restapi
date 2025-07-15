package shop.mtcoding.blog.course.application.port.in.dto;

import org.springframework.data.domain.Page;
import shop.mtcoding.blog.course.domain.Course;
import shop.mtcoding.blog.course.domain.CourseStudent;
import shop.mtcoding.blog.course.domain.CourseSubject;

import java.util.List;

public class CourseOutput {
    public record MaxPage(Page<Course> coursePG) {
    }

    public record Max(Course course) {
    }

    public record Detail(Course course, List<CourseSubject> subjects, List<CourseStudent> students) {
    }
}
