package shop.mtcoding.blog.course.application.port.out;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shop.mtcoding.blog.course.domain.Course;
import shop.mtcoding.blog.course.domain.CourseStudent;
import shop.mtcoding.blog.course.domain.Subject;

import java.util.List;
import java.util.Optional;

public interface CourseRepositoryPort {
    Optional<Course> findById(Long id);

    Page<Course> findAllByTeacherId(Long teacherId, Pageable pageable);

    List<Course> findAllNotFinished();

    Course save(Course course);

    List<Subject> findAllSubjectsByCourseId(Long courseId);

    List<CourseStudent> findAllStudentsByCourseId(Long courseId);
}
