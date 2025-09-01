package shop.mtcoding.blog.course.application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.mtcoding.blog.course.application.domain.CourseStudent;

import java.util.List;
import java.util.Optional;

public interface CourseStudentRepository extends JpaRepository<CourseStudent, Long> {
    Optional<CourseStudent> findByStudentId(Long studentId);

    List<CourseStudent> findAllByCourseId(Long courseId);
}
