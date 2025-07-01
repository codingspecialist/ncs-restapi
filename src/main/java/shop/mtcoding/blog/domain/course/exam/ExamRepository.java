package shop.mtcoding.blog.domain.course.exam;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import shop.mtcoding.blog.domain.course.subject.paper.PaperVersion;

import java.util.List;
import java.util.Optional;

public interface ExamRepository extends JpaRepository<Exam, Long> {

    List<Exam> findByStudentIdInAndSubjectId(List<Long> studentIds, Long subjectId);

    @Query("select ex from Exam ex left join fetch ex.paper p left join fetch p.subject sb where sb.id = :subjectId and p.paperVersion = :paperVersion")
    List<Exam> findAllBySubjectIdAndPaperVersion(Long subjectId, PaperVersion paperVersion);

    @Query("select ex from Exam ex where ex.paper.subject.id = :subjectId and ex.student.id = :studentId and ex.isActive = :isActive")
    Optional<Exam> findBySubjectIdAndStudentIdAndIsUse(@Param("subjectId") Long subjectId, @Param("studentId") Long studentId, @Param("isActive") Boolean isActive);

    @Query("select ex from Exam ex left join fetch ex.paper p join fetch p.subject sb join fetch ex.student st join fetch st.user u where sb.id = :subjectId and ex.isActive = true order by st.name")
    List<Exam> findBySubjectIdAndIsUseOrderByStudentNameAsc(@Param("subjectId") Long subjectId);


    @Query("select ex from Exam ex join fetch ex.paper p join fetch ex.student st join fetch st.user u where ex.paper.subject.id = :subjectId order by st.name asc")
    List<Exam> findAllBySubjectId(@Param("subjectId") Long subjectId);


    @Query("select ex from Exam ex join fetch ex.paper p join fetch ex.student st where ex.student.id = :studentId order by p.subject.no asc")
    List<Exam> findAllByStudentId(@Param("studentId") Long studentId);
}
