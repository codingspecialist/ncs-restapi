package shop.mtcoding.blog.user.application.port.out;

import shop.mtcoding.blog.user.domain.User;

import java.util.List;
import java.util.Optional;

// findUserPort처럼 추상적으로 만드는 것이 일반적으로 더 좋은 접근 방식입니다. 이것이 바로 헥사고날 아키텍처가 추구하는 캡슐화와 도메인 코어의 독립성
// 즉 Port이름은 추상적이어야 한다. (어디서 찾는지 몰라도 된다)
// 대신 역할이 다르면 따로 만들어야 한다. (User저장소Port, User이벤트Port)
public interface UserRepositoryPort {
    Optional<User> findByUsernameAndPassword(String username, String password);

    Optional<User> findById(Long id);

    Optional<User> findByUsername(String username);

    User save(User user);

    Optional<User> findByTeacherId(Long teacherId);

    List<User> findByTeacherIdIn(List<Long> teacherIds);
}
