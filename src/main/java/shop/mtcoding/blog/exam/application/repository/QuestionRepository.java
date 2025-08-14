package shop.mtcoding.blog.exam.application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import shop.mtcoding.blog.exam.application.domain.Question;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    
    @Query("select q from Question q join fetch q.questionOptions op where q.paper.id = :paperId")
    List<Question> findAllByPaperId(@Param("paperId") Long paperId);
}
