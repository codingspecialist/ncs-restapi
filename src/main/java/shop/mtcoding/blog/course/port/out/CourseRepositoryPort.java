package shop.mtcoding.blog.course.port.out;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shop.mtcoding.blog.course.model.Course;

import java.util.List;
import java.util.Optional;

public interface CourseRepositoryPort {
    Optional<Course> findById(Long id);

    Page<Course> findAllByTeacherId(Long teacherId, Pageable pageable);

    List<Course> findAllNotFinished();
}
