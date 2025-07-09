package shop.mtcoding.blog.course.application.port.out;

import shop.mtcoding.blog.user.domain.User;

import java.util.List;

public interface LoadUserPort {
    User loadUser(Long userId);

    User loadUserByTeacherId(Long teacherId);

    List<User> loadUserByTeacherIdIn(List<Long> teacherIds);
}
