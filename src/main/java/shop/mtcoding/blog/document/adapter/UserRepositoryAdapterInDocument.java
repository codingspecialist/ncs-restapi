package shop.mtcoding.blog.document.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import shop.mtcoding.blog.user.application.domain.Teacher;
import shop.mtcoding.blog.user.application.domain.User;
import shop.mtcoding.blog.user.application.repository.UserRepository;

import java.util.Optional;

@Component("userRepositoryAdapterInDocument")
@RequiredArgsConstructor
public class UserRepositoryAdapterInDocument {

    private final UserRepository userRepository;

    public Optional<User> findUserById(Long userId) {
        return userRepository.findById(userId);
    }

    public Optional<User> findUserByTeacherId(Long teacherId) {
        return userRepository.findByTeacherId(teacherId);
    }

    public Teacher getTeacherFromUser(User user) {
        return user.getTeacher();
    }
}
