package shop.mtcoding.blog.domain.course.subject.paper;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PaperRepository extends JpaRepository<Paper, Long> {

    @Query("SELECT p FROM Paper p WHERE p.subject.id = :subjectId order by p.evaluationDate asc")
    Page<Paper> findAllBySubjectId(@Param("subjectId") Long subjectId, Pageable pageable);

    @Query("SELECT p FROM Paper p WHERE p.subject.course.id = :courseId")
    Page<Paper> findAllByCourseId(@Param("courseId") Long courseId, Pageable pageable);

    /// ///////////////////// 구형들
    @Query("select p from Paper p join fetch p.subject sb where p.subject.course.id = :courseId")
    List<Paper> findByCourseId(@Param("courseId") Long courseId);

    @Query("select p from Paper p where p.subject.id = :subjectId and p.paperType = :paperType")
    List<Paper> findBySubjectIdAndPaperType(@Param("subjectId") Long subjectId, @Param("paperType") PaperType paperType);

    List<Paper> findBySubjectId(@Param("subjectId") Long subjectId);
}
