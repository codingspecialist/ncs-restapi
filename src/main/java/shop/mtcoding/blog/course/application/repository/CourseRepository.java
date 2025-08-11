package shop.mtcoding.blog.course.application.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import shop.mtcoding.blog.course.application.domain.Course;
import shop.mtcoding.blog.course.application.domain.CourseStudent;
import shop.mtcoding.blog.course.application.domain.Subject;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {

    Optional<Course> findById(Long id);

    @Query("SELECT c FROM Course c WHERE c.courseStatus <> 'FINISHED'")
    List<Course> findAllNotFinished();

    @Query("SELECT ct.course FROM CourseTeacher ct WHERE ct.teacher.id = :teacherId")
    Page<Course> findAllByTeacherId(@Param("teacherId") Long teacherId, Pageable pageable);

    Course save(Course course);

    List<Subject> findAllSubjectsByCourseId(Long courseId);

    List<CourseStudent> findAllStudentsByCourseId(Long courseId);
}
