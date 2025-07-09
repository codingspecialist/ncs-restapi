package shop.mtcoding.blog.course.adapter.out.external;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import shop.mtcoding.blog.course.application.port.out.LoadUserPort;
import shop.mtcoding.blog.user.application.port.out.UserRepositoryPort;
import shop.mtcoding.blog.user.domain.User;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserRepositoryAdapter implements LoadUserPort {

    private final UserRepositoryPort userRepositoryPort;


    @Override
    public User loadUser(Long userId) {
        return userRepositoryPort.findById(userId).get();
    }

    @Override
    public User loadUserByTeacherId(Long teacherId) {
        return userRepositoryPort.findByTeacherId(teacherId).get();
    }

    @Override
    public List<User> loadUserByTeacherIdIn(List<Long> teacherIds) {
        return userRepositoryPort.findByTeacherIdIn(teacherIds);
    }
}
