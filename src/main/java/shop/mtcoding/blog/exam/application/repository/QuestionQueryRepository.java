package shop.mtcoding.blog.exam.application.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import shop.mtcoding.blog.exam.web.dto.PaperResponse;

@RequiredArgsConstructor
@Repository
public class QuestionQueryRepository {
    private final EntityManager em;

    public PaperResponse.NextQuestion findNextNo(Long paperId) {
        String sql = """
                    SELECT
                        IFNULL(MAX(no) + 1, 1) AS expectNo
                    FROM
                        question_tb
                    WHERE
                        paper_id = ?
                """;

        Query query = em.createNativeQuery(sql);
        query.setParameter(1, paperId);

        Object obs = query.getSingleResult();
        int expectNo = (Integer) obs;

        return new PaperResponse.NextQuestion(expectNo, paperId);
    }
}
