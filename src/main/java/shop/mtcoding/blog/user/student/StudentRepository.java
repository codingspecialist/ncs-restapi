package shop.mtcoding.blog.user.student;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {

    @Query("select st from Student st join fetch st.user u where st.course.id = :courseId order by u.name asc")
    List<Student> findByCourseId(@Param("courseId") Long courseId);
    
    Student findByUserId(@Param("userId") Long userId);

    Optional<Student> findByAuthCode(@Param("authCode") String authCode);
}
