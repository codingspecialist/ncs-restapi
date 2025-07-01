package shop.mtcoding.blog.domain.user.application.port.in;

import shop.mtcoding.blog.domain.user.application.dto.UserCommand;
import shop.mtcoding.blog.domain.user.application.dto.UserResult;

public interface LoginUseCase {
    UserResult.Login 로그인(UserCommand.Login command);
}
