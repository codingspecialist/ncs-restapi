package shop.mtcoding.blog.domain.course.exam;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import shop.mtcoding.blog.domain.course.subject.paper.PaperType;

import java.util.List;
import java.util.Optional;

public interface ExamRepository extends JpaRepository<Exam, Long> {

    @Query(value = """
            SELECT e.*
            FROM exam_tb e
            RIGHT JOIN student_tb s 
              ON s.id = e.student_id AND e.subject_id = :subjectId AND e.is_use = true
            """, nativeQuery = true)
    List<Exam> findValidExamsBySubjectId(@Param("subjectId") Long subjectId);


    @Query("select ex from Exam ex left join fetch ex.paper p left join fetch p.subject sb where sb.id = :subjectId and p.paperType = :paperType")
    List<Exam> findAllBySubjectIdAndEvaluationWay(Long subjectId, PaperType paperType);

    @Query("select ex from Exam ex where ex.paper.subject.id = :subjectId and ex.student.id = :studentId and ex.isUse = :isUse")
    Optional<Exam> findBySubjectIdAndStudentIdAndIsUse(@Param("subjectId") Long subjectId, @Param("studentId") Long studentId, @Param("isUse") Boolean isUse);

    @Query("select ex from Exam ex left join fetch ex.paper p join fetch p.subject sb join fetch ex.student st join fetch st.user u where sb.id = :subjectId and ex.isUse = true order by st.name")
    List<Exam> findBySubjectIdAndIsUseOrderByStudentNameAsc(@Param("subjectId") Long subjectId);


    @Query("select ex from Exam ex join fetch ex.paper p join fetch ex.student st join fetch st.user u where ex.paper.subject.id = :subjectId order by st.name asc")
    List<Exam> findAllBySubjectId(@Param("subjectId") Long subjectId);


    @Query("select ex from Exam ex join fetch ex.paper p join fetch ex.student st where ex.student.id = :studentId order by p.subject.no asc")
    List<Exam> findByStudentId(@Param("studentId") Long studentId);
}
