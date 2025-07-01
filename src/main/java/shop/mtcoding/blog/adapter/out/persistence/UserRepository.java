package shop.mtcoding.blog.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.mtcoding.blog.domain.user.application.port.out.FindUserPort;
import shop.mtcoding.blog.domain.user.application.port.out.SaveUserPort;
import shop.mtcoding.blog.domain.user.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, FindUserPort, SaveUserPort {
    @Override
    Optional<User> findByUsernameAndPassword(String username, String password);

    @Override
    Optional<User> findByUsername(String username);

    @Override
    Optional<User> findById(Long id);

    @Override
    User save(User user);
}
