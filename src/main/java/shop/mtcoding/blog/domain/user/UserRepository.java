package shop.mtcoding.blog.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {

    @Query("""
                select u from User u
                left join fetch u.student
                left join fetch u.teacher
                where u.username = :username and u.password = :password
            """)
    Optional<User> findByUsernameAndPassword(@Param("username") String username, @Param("password") String password);

    Optional<User> findByUsername(String username);

    @Query("""
            select u from User u
            left join fetch u.student
            left join fetch u.teacher
            where u.username = :username
            """)
    Optional<User> findByUsernameWithStudentOrTeacher(String username);
}