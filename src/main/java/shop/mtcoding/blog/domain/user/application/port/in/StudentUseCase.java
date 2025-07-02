package shop.mtcoding.blog.domain.user.application.port.in;

import shop.mtcoding.blog.domain.user.application.port.in.dto.UserCommand;
import shop.mtcoding.blog.domain.user.domain.User;

public interface StudentUseCase {
    User 학생회원가입(UserCommand.StudentJoin command);
}
