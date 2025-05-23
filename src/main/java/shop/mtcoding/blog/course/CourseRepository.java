package shop.mtcoding.blog.course;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {

    @Query("select c from Course c left join fetch c.subjects sts where c.id=:courseId")
    Optional<Course> findByIdWithSubject(@Param("courseId") Long courseId);

    @Query("SELECT ct.course FROM CourseTeacher ct WHERE ct.teacher.id = :teacherId")
    Page<Course> findAllByTeacherId(@Param("teacherId") Long teacherId, Pageable pageable);

    @Query("SELECT c FROM Course c WHERE c.courseStatus <> 'FINISHED'")
    List<Course> findAllNotFinished();
}
