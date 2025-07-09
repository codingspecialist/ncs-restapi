package shop.mtcoding.blog.user.application.port.in;

import shop.mtcoding.blog.user.application.port.in.dto.UserCommand;
import shop.mtcoding.blog.user.application.port.in.dto.UserOutput;

public interface TeacherUseCase {
    UserOutput.Max 강사회원가입(UserCommand.TeacherJoin command);
}
