package shop.mtcoding.blog.domain.course.subject;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SubjectRepository extends JpaRepository<Subject, Long> {

    @Query("select sb from Subject sb where sb.course.id = :courseId")
    List<Subject> findByCourseId(@Param("courseId") Long courseId);

    @Query("select sb from Subject sb join fetch sb.course cs")
    Page<Subject> findAll(Pageable pageable);
}
