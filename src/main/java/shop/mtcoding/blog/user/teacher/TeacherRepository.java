package shop.mtcoding.blog.user.teacher;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {

    Teacher findByUserId(@Param("userId") Long userId);
}
