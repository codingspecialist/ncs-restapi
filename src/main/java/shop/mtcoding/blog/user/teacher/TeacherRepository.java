package shop.mtcoding.blog.user.teacher;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {

    Teacher findByUserId(@Param("userId") Long userId);

    @Query("select tc from Teacher tc join fetch tc.user u where u.name = :name")
    Optional<Teacher> findByName(@Param("name") String name);
}
