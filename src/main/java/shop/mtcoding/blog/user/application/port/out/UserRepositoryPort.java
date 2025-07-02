package shop.mtcoding.blog.user.application.port.out;

import shop.mtcoding.blog.user.domain.User;

import java.util.Optional;

public interface UserRepositoryPort {
    Optional<User> findByUsernameAndPassword(String username, String password);

    Optional<User> findById(Long id);

    Optional<User> findByUsername(String username);

    User save(User user);
}
