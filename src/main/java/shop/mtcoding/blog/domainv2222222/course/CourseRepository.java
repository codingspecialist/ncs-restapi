package shop.mtcoding.blog.domainv2222222.course;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {

    // TODO : 직원 id로 검색해서 볼 수 있어야함

    @Query("SELECT ct.course FROM CourseTeacher ct WHERE ct.teacher.id = :teacherId")
    Page<Course> findAllByTeacherId(@Param("teacherId") Long teacherId, Pageable pageable);

    @Query("SELECT c FROM Course c WHERE c.courseStatus <> 'FINISHED'")
    List<Course> findAllNotFinished();
}
