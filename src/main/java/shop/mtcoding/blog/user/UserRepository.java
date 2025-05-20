package shop.mtcoding.blog.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {

    // 유저네임, 패스워드로 조회시에는 teacher, student를 함께 outer join 하기
    @Query("""
                select u from User u 
                left join fetch u.student 
                left join fetch u.teacher 
                where u.username = :username and u.password = :password
            """)
    Optional<User> findByUsernameAndPassword(@Param("username") String username, @Param("password") String password);

    // 유저네임으로 조회시에는 teacher, student를 함께 outer join 하기
    @Query("""
            select u from User u 
            left join fetch u.student 
            left join fetch u.teacher 
            where u.username = :username
            """)
    Optional<User> findByUsername(String username);

    // 선생님으로 조회하면 teacher를 정해서 조인하기
    @Query("select u from User u join fetch u.teacher tc where u.name = :teacherName")
    Optional<User> findByTeacherName(@Param("teacherName") String teacherName);
}