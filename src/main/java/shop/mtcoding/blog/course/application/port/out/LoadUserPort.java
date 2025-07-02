package shop.mtcoding.blog.course.application.port.out;

import shop.mtcoding.blog.user.domain.User;

public interface LoadUserPort {
    User loadUser(Long userId);
}
