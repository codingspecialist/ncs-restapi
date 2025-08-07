package shop.mtcoding.blog.course.application.port.out;

import org.springframework.data.repository.query.Param;
import shop.mtcoding.blog.course.domain.Subject;

import java.util.List;
import java.util.Optional;

public interface SubjectRepositoryPort {
    Optional<Subject> findById(@Param("id") Long id);

    List<Subject> findAllByCourseId(@Param("courseId") Long courseId);

    Optional<Subject> findByIdWithElements(@Param("subjectId") Long subjectId);

    Subject save(Subject subject);
}
