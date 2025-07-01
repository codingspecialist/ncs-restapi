package shop.mtcoding.blog.domain.course.application.port.out;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shop.mtcoding.blog.domain.course.model.Course;

import java.util.List;
import java.util.Optional;

public interface FindCoursePort {
    Optional<Course> findById(Long id);

    Page<Course> findAllByTeacherId(Long teacherId, Pageable pageable);

    List<Course> findAllNotFinished();
}
