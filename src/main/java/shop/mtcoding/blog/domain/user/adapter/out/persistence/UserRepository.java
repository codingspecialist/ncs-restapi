package shop.mtcoding.blog.domain.user.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.mtcoding.blog.domain.user.application.port.out.UserRepositoryPort;
import shop.mtcoding.blog.domain.user.domain.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryPort {
    @Override
    Optional<User> findByUsernameAndPassword(String username, String password);

    @Override
    Optional<User> findByUsername(String username);

    @Override
    Optional<User> findById(Long id);

    @Override
    User save(User user);
}
