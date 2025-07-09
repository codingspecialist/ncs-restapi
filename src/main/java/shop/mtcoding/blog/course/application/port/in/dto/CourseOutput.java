package shop.mtcoding.blog.course.application.port.in.dto;

import org.springframework.data.domain.Page;
import shop.mtcoding.blog.course.domain.Course;

public class CourseOutput {
    public record MaxPage(Page<Course> coursePG) {
    }

    public record Max(Course course) {
    }
}
