package shop.mtcoding.blog.course.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import shop.mtcoding.blog.user.adapter.out.persistence.UserRepository;
import shop.mtcoding.blog.user.domain.User;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserRepositoryAdapter {

    private final UserRepository userRepository;

    public User loadUser(Long userId) {
        return userRepository.findById(userId).get();
    }

    public User loadUserByTeacherId(Long teacherId) {
        return userRepository.findByTeacherId(teacherId).get();
    }

    public List<User> loadUserByTeacherIdIn(List<Long> teacherIds) {
        return userRepository.findByTeacherIdIn(teacherIds);
    }
}
