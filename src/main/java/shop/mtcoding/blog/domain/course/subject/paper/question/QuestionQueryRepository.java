package shop.mtcoding.blog.domain.course.subject.paper.question;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@RequiredArgsConstructor
@Repository
public class QuestionQueryRepository {
    private final EntityManager em;

    public QuestionDBResponse.ExpectedNextDTO findStatisticsByPaperId(Long paperId) {
        String sql = "SELECT ifnull(max(no) + 1, 1) as expectNo, (SELECT 100.0 / count FROM paper_tb WHERE id = ?) as expectPoint FROM question_tb WHERE paper_id = ?";
        Query query = em.createNativeQuery(sql);
        query.setParameter(1, paperId);
        query.setParameter(2, paperId);

        Object[] obs = (Object[]) query.getSingleResult();
        int expectNo = (Integer) obs[0];
        BigDecimal expectPoint = (BigDecimal) obs[1];

        return new QuestionDBResponse.ExpectedNextDTO(expectNo, expectPoint.intValue());
    }
}
