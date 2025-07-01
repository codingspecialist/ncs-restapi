package shop.mtcoding.blog.domain.course.subject.paper;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PaperRepository extends JpaRepository<Paper, Long> {

 
    @Query("SELECT COUNT(p) > 0 FROM Paper p WHERE p.subject.id = :subjectId AND p.paperVersion = :paperVersion")
    boolean existsBySubjectIdAndPaperVersion(@Param("subjectId") Long subjectId, @Param("paperVersion") PaperVersion paperVersion);

    @Query("SELECT p FROM Paper p WHERE p.subject.id = :subjectId order by p.evaluationDate asc")
    List<Paper> findAllBySubjectId(@Param("subjectId") Long subjectId);

    @Query("select p from Paper p join fetch p.subject sb where p.subject.course.id = :courseId")
    List<Paper> findAllByCourseId(@Param("courseId") Long courseId);

    @Query("select p from Paper p where p.subject.id = :subjectId and p.paperVersion = :paperVersion")
    Optional<Paper> findBySubjectIdAndPaperVersion(@Param("subjectId") Long subjectId, @Param("paperVersion") PaperVersion paperVersion);

}
