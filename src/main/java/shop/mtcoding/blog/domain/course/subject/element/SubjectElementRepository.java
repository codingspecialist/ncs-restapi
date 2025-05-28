package shop.mtcoding.blog.domain.course.subject.element;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SubjectElementRepository extends JpaRepository<SubjectElement, Long> {

    @Query("select se from SubjectElement se where se.subject.id = :subjectId")
    List<SubjectElement> findBySubjectId(@Param("subjectId") Long subjectId);

    @Query("SELECT se.no FROM SubjectElement se WHERE se.subject.id = :subjectId AND se.no IN :nos")
    List<Integer> findExistingNosBySubjectIdAndNoIn(@Param("subjectId") Long subjectId, @Param("nos") List<Integer> nos);
}
