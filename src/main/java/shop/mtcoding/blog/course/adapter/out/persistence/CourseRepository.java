package shop.mtcoding.blog.course.adapter.out.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import shop.mtcoding.blog.course.application.port.out.CourseRepositoryPort;
import shop.mtcoding.blog.course.domain.Course;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long>, CourseRepositoryPort {
    @Override
    Optional<Course> findById(Long id);

    // 스케줄러가 과정상태 변경할 때 필요
    @Override
    @Query("SELECT c FROM Course c WHERE c.courseStatus <> 'FINISHED'")
    List<Course> findAllNotFinished();

    @Override
    @Query("SELECT ct.course FROM CourseTeacher ct WHERE ct.teacher.id = :teacherId")
    Page<Course> findAllByTeacherId(@Param("teacherId") Long teacherId, Pageable pageable);

    @Override
    Course save(Course course);
}
