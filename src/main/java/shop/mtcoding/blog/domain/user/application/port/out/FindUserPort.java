package shop.mtcoding.blog.domain.user.application.port.out;

import shop.mtcoding.blog.domain.user.model.User;

import java.util.Optional;

public interface FindUserPort {
    Optional<User> findByUsernameAndPassword(String username, String password);

    Optional<User> findById(Long id);

    Optional<User> findByUsername(String username);
}
