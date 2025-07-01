package shop.mtcoding.blog.domain.user.application.port.out;

import shop.mtcoding.blog.domain.user.model.User;

public interface SaveUserPort {
    User save(User user);
}
