package shop.mtcoding.blog.user.application.port.in;

import shop.mtcoding.blog.user.application.port.in.dto.UserCommand;
import shop.mtcoding.blog.user.domain.User;

public interface EmpUseCase {
    User 직원회원가입(UserCommand.EmpJoin command);
}
