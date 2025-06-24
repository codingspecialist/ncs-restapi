package shop.mtcoding.blog.domain.user.teacher;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {

    @Query("select tc from Teacher tc join fetch tc.user u")
    List<Teacher> findAllWithUser();

    Optional<Teacher> findByUserId(Long userId);

    @Query("select tc from Teacher tc join fetch tc.user u where tc.name = :name")
    Optional<Teacher> findByName(@Param("name") String name);
}
