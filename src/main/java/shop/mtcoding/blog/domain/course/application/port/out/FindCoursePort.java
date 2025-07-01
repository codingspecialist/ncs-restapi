package shop.mtcoding.blog.domain.course.application.port.out;

import shop.mtcoding.blog.domain.course.model.Course;

import java.util.Optional;

public interface FindCoursePort {
    Optional<Course> findById(Long id);
}
