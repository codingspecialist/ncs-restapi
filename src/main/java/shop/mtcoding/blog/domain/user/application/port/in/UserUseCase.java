package shop.mtcoding.blog.domain.user.application.port.in;

import shop.mtcoding.blog.domain.user.application.port.in.dto.UserCommand;
import shop.mtcoding.blog.domain.user.application.port.in.dto.UserOutput;

public interface UserUseCase {
    UserOutput.SessionItem 로그인(UserCommand.Login command);
}
