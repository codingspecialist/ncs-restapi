package shop.mtcoding.blog.adapter.out;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.mtcoding.blog.domain.course.application.port.out.FindCoursePort;
import shop.mtcoding.blog.domain.course.model.Course;

import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long>, FindCoursePort {
    @Override
    Optional<Course> findById(Long id);
}
