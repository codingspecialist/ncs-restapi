package shop.mtcoding.blog.course.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import shop.mtcoding.blog.course.application.port.out.SubjectElementRepositoryPort;
import shop.mtcoding.blog.course.domain.SubjectElement;

import java.util.List;

public interface SubjectElementRepository extends JpaRepository<SubjectElement, Long>, SubjectElementRepositoryPort {

    @Query("select se from SubjectElement se where se.subject.id = :subjectId")
    List<SubjectElement> findAllBySubjectId(@Param("subjectId") Long subjectId);

    @Query("SELECT se.no FROM SubjectElement se WHERE se.subject.id = :subjectId AND se.no IN :nos")
    List<Integer> findNosBySubjectIdAndNoIn(@Param("subjectId") Long subjectId, @Param("nos") List<Integer> nos);
}
