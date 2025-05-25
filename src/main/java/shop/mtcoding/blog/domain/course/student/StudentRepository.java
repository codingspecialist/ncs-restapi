package shop.mtcoding.blog.domain.course.student;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {

    @Query("select st from Student st where st.course.id = :courseId order by st.name asc")
    List<Student> findByCourseId(@Param("courseId") Long courseId);

    Student findByUserId(@Param("userId") Long userId);

    @Query("select st from Student st where st.authCode = :authCode and st.birthday = :birthday and st.isVerified is null")
    Optional<Student> findByAuthCodeAndBirthdayAndIsNotVerified(@Param("authCode") String authCode, @Param("birthday") String birthday);

    @Query("select st from Student st where st.authCode = :authCode")
    Optional<Student> findByAuthCode(@Param("authCode") String authCode);

}
