package shop.mtcoding.blog.course.port.out;

import shop.mtcoding.blog.course.model.UserInfo;

public interface LoadUserPort {
    UserInfo loadUser(Long userId);
}
