package shop.mtcoding.blog.paper;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PaperRepository extends JpaRepository<Paper, Long> {

    @Query("select p from Paper p join fetch p.subject sb where p.subject.course.id = :courseId")
    List<Paper> findByCourseId(@Param("courseId") Long courseId);

    @Query("select p from Paper p where p.subject.id = :subjectId and p.isReEvaluation = :isReEvaluation")
    List<Paper> findBySubjectIdAndPaperState(@Param("subjectId") Long subjectId, @Param("isReEvaluation") Boolean isReEvaluation);

    List<Paper> findBySubjectId(@Param("subjectId") Long subjectId);
}
