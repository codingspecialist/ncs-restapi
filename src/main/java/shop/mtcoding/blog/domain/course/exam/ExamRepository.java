package shop.mtcoding.blog.domain.course.exam;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ExamRepository extends JpaRepository<Exam, Long> {

    @Query("select ex from Exam ex left join fetch ex.paper p left join fetch p.subject sb where p.isReEvaluation = :isReEvaluation and sb.id = :subjectId")
    List<Exam> findBySubjectIdAndEvaludationType(Boolean isReEvaluation, Long subjectId); // isReEvaluation = 본평가, 재평가

    @Query("select ex from Exam ex where ex.paper.subject.id = :subjectId and ex.student.id = :studentId and ex.isUse = :isUse")
    Optional<Exam> findBySubjectIdAndStudentIdAndIsNotUse(@Param("subjectId") Long subjectId, @Param("studentId") Long studentId, @Param("isUse") Boolean isUse);

    @Query("select ex from Exam ex left join fetch ex.paper p join fetch p.subject sb join fetch ex.student st join fetch st.user u where sb.id = :subjectId and ex.isUse = true order by st.name")
    List<Exam> findBySubjectIdAndIsUseOrderByStudentNameAsc(@Param("subjectId") Long subjectId);


    @Query("select ex from Exam ex join fetch ex.paper p join fetch ex.student st join fetch st.user u where ex.paper.subject.id = :subjectId order by st.name asc")
    List<Exam> findBySubjectId(@Param("subjectId") Long subjectId);


    @Query("select ex from Exam ex join fetch ex.paper p join fetch ex.student st where ex.student.id = :studentId order by p.subject.no asc")
    List<Exam> findByStudentId(@Param("studentId") Long studentId);

    Optional<Exam> findByPaperIdAndStudentId(@Param("paperId") Long paperId, @Param("studentId") Long studentId);

    @Query("select ex from Exam ex join fetch ex.examAnswers an join fetch an.question q join fetch q.questionOptions op where ex.id = :examId")
    Optional<Exam> findByIdWithAnswer(@Param("examId") Long examId);
}
