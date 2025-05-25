package shop.mtcoding.blog.domain.course.subject.paper.question;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    // 다음 예상 번호, 평균 점수
    // select max(no)+1 expectNo, (select 100/count from paper_tb where id = 1) expectPoint from question_tb where paper_id = 1;

    @Query("select q from Question q join fetch q.questionOptions op where q.paper.id = :paperId")
    List<Question> findByPaperId(@Param("paperId") Long paperId);
}
