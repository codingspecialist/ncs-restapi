package shop.mtcoding.blog.domain.user.application.port.in;

import shop.mtcoding.blog.domain.user.application.dto.UserCommand;
import shop.mtcoding.blog.domain.user.application.dto.UserResult;

public interface StudentJoinUseCase {
    UserResult.StudentJoin 학생회원가입(UserCommand.StudentJoin command);
}
