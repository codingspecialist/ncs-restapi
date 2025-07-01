package shop.mtcoding.blog.domainv2222222.course.subject.paper.question;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import shop.mtcoding.blog.domainv2222222.course.subject.paper.PaperModel;

@RequiredArgsConstructor
@Repository
public class QuestionQueryRepository {
    private final EntityManager em;

    public PaperModel.NextQuestion findNextNo(Long paperId) {
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

        return new PaperModel.NextQuestion(expectNo, paperId);
    }
}
