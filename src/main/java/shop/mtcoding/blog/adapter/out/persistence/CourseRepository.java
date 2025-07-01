package shop.mtcoding.blog.adapter.out.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import shop.mtcoding.blog.domain.course.application.port.out.FindCoursePort;
import shop.mtcoding.blog.domain.course.model.Course;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long>, FindCoursePort {
    @Override
    Optional<Course> findById(Long id);

    @Override
    @Query("SELECT c FROM Course c WHERE c.courseStatus <> 'FINISHED'")
    List<Course> findAllNotFinished();

    @Override
    @Query("SELECT ct.course FROM CourseTeacher ct WHERE ct.teacher.id = :teacherId")
    Page<Course> findAllByTeacherId(@Param("teacherId") Long teacherId, Pageable pageable);
}
