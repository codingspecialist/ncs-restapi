package shop.mtcoding.blog.domain.course.exam;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class ExamQueryRepository {
    private final EntityManager em;

    public List<ExamModel.Result> findExamResult(Long subjectId, Long courseId) {
        String sql = """
                    SELECT 
                      e.id,
                      s.name AS student_name,
                      sb.title AS subject_title,
                      e.exam_state,
                      e.teacher_name,
                      e.score,
                      e.grade,
                      e.pass_state,
                      e.re_exam_reason,
                      e.standby,
                      s.id AS student_id,
                      e.paper_id AS paper_id,
                    FROM exam_tb e
                    RIGHT OUTER JOIN student_tb s
                      ON s.id = e.student_id
                      AND e.subject_id = ?
                      AND e.is_use = true
                    LEFT JOIN subject_tb sb ON e.subject_id = sb.id
                    where s.course_id = ?
                    ORDER BY s.name
                """;

        Query query = em.createNativeQuery(sql);
        query.setParameter(1, subjectId);
        query.setParameter(2, courseId);

        List<Object[]> rows = query.getResultList();
        List<ExamModel.Result> resultList = new ArrayList<>();

        for (Object[] row : rows) {
            Long examId = row[0] != null ? ((Number) row[0]).longValue() : null;
            String studentName = (String) row[1];
            String subjectTitle = (String) row[2];
            String examState = (String) row[3];
            String teacherName = (String) row[4];
            Double score = row[5] != null ? ((Number) row[5]).doubleValue() : null;
            Integer grade = row[6] != null ? ((Number) row[6]).intValue() : null;
            String passState = (String) row[7];
            String reExamReason = row[8] != null ? row[8].toString() : "";
            Boolean standby = (Boolean) row[9];
            Long studentId = row[10] != null ? ((Number) row[10]).longValue() : null;
            Long paperId = row[11] != null ? ((Number) row[11]).longValue() : null;
            Boolean isAbsent = false;

            resultList.add(new ExamModel.Result(examId,
                    studentName, subjectTitle, examState, teacherName,
                    score, grade, passState, reExamReason, studentId, paperId, isAbsent, standby
            ));


        }

        return resultList;
    }


}
