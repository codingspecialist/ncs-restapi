package shop.mtcoding.blog.exam.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import shop.mtcoding.blog.user.application.domain.User;
import shop.mtcoding.blog.user.application.repository.UserRepository;

import java.util.Optional;

@Component("userRepositoryAdapterInExam")
@RequiredArgsConstructor
public class UserRepositoryAdapterInExam {

    private final UserRepository userRepository;

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
}
