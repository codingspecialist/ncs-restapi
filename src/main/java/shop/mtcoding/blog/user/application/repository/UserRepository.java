package shop.mtcoding.blog.user.application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.mtcoding.blog.user.application.domain.User;

import java.util.List;
import java.util.Optional;

// 어그리게이트 루트의 Repository만 만드는 것이 권장됨
// StudentQueryRepository 는 아주 예외적으로 특정 역할(예: Student)에 특화된 쿼리나 조회가 빈번하고,
// 그 쿼리가 User 어그리게이트의 로딩을 동반할 필요가 없을 때만 고려해볼 수 있습니다.
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsernameAndPassword(String username, String password);

    Optional<User> findByUsername(String username);

    Optional<User> findById(Long id);

    User save(User user);

    Optional<User> findByTeacherId(Long teacherId);

    List<User> findByTeacherIdIn(List<Long> teacherIds);
}
