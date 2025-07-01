package shop.mtcoding.blog.domain.user.application.port.in;

import shop.mtcoding.blog.domain.user.application.dto.UserCommand;
import shop.mtcoding.blog.domain.user.application.dto.UserResult;

public interface TeacherJoinUseCase {
    UserResult.TeacherJoin 강사회원가입(UserCommand.TeacherJoin command);
}
