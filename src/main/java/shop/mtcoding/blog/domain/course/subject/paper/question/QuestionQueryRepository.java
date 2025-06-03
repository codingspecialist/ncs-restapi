package shop.mtcoding.blog.domain.course.subject.paper.question;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import shop.mtcoding.blog.domain.course.subject.paper.PaperModel;

import java.math.BigDecimal;

@RequiredArgsConstructor
@Repository
public class QuestionQueryRepository {
    private final EntityManager em;

    public PaperModel.NextQuestion findStatisticsByPaperId(Long paperId) {
        String sql = """
                    SELECT 
                        IFNULL(MAX(no) + 1, 1) AS expectNo,
                        (SELECT 100.0 / question_count FROM paper_tb WHERE id = ?) AS expectPoint
                    FROM 
                        question_tb 
                    WHERE 
                        paper_id = ?
                """;

        Query query = em.createNativeQuery(sql);
        query.setParameter(1, paperId);
        query.setParameter(2, paperId);

        Object[] obs = (Object[]) query.getSingleResult();
        int expectNo = (Integer) obs[0];
        BigDecimal expectPoint = (BigDecimal) obs[1];

        return new PaperModel.NextQuestion(expectNo, expectPoint.intValue(), paperId);
    }
}
