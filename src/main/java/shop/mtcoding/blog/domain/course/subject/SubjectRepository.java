package shop.mtcoding.blog.domain.course.subject;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SubjectRepository extends JpaRepository<Subject, Long> {

    @Query("select sb from Subject sb where sb.course.id = :courseId")
    List<Subject> findAllByCourseId(@Param("courseId") Long courseId);

    @Query("select sb from Subject sb join fetch sb.elements els where sb.id = :subjectId")
    Optional<Subject> findByIdWithElements(@Param("subjectId") Long subjectId);
}
