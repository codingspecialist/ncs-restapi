package shop.mtcoding.blog.user.application.port.in;

import shop.mtcoding.blog.user.application.port.in.dto.UserCommand;
import shop.mtcoding.blog.user.application.port.in.dto.UserOutput;

public interface UserUseCase {
    UserOutput.Login 로그인(UserCommand.Login command);
}
