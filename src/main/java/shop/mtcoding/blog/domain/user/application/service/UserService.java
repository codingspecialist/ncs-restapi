package shop.mtcoding.blog.domain.user.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.mtcoding.blog._core.errors.exception.api.Exception400;
import shop.mtcoding.blog._core.errors.exception.api.Exception401;
import shop.mtcoding.blog._core.errors.exception.api.Exception404;
import shop.mtcoding.blog._core.utils.JwtUtil;
import shop.mtcoding.blog._core.utils.MyUtil;
import shop.mtcoding.blog.domain.course.application.port.out.FindCoursePort;
import shop.mtcoding.blog.domain.course.model.Course;
import shop.mtcoding.blog.domain.user.application.dto.UserCommand;
import shop.mtcoding.blog.domain.user.application.dto.UserResult;
import shop.mtcoding.blog.domain.user.application.port.in.LoginUseCase;
import shop.mtcoding.blog.domain.user.application.port.in.StudentJoinUseCase;
import shop.mtcoding.blog.domain.user.application.port.in.TeacherJoinUseCase;
import shop.mtcoding.blog.domain.user.application.port.out.FindUserPort;
import shop.mtcoding.blog.domain.user.application.port.out.SaveUserPort;
import shop.mtcoding.blog.domain.user.model.User;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements
        LoginUseCase, StudentJoinUseCase, TeacherJoinUseCase {

    // UserRepository의 구현체를 DI
    private final FindUserPort findUserPort;
    private final SaveUserPort saveUserPort;
    private final FindCoursePort findCoursePort;

    @Override
    public UserResult.Login 로그인(UserCommand.Login command) {
        User user = findUserPort.findByUsernameAndPassword(
                        command.username(), command.password())
                .orElseThrow(() -> new Exception401("인증되지 않았습니다"));

        String accessToken = JwtUtil.create(user);
        String refreshToken = JwtUtil.createRefresh(user);

        return new UserResult.Login(user, accessToken, refreshToken);
    }

    @Transactional
    @Override
    public UserResult.StudentJoin 학생회원가입(UserCommand.StudentJoin command) {
        Optional<User> userOP = findUserPort.findByUsername(command.username());
        if (userOP.isPresent()) {
            throw new Exception400("중복된 유저네임입니다.");
        }

        Course course = findCoursePort.findById(command.courseId())
                .orElseThrow(() -> new Exception404("조회된 과정이 없습니다."));
        String authCode = MyUtil.generateAuthCode();
        User newUser = User.from(command, course, authCode);
        User savedUser = saveUserPort.save(newUser);

        return new UserResult.StudentJoin(savedUser);
    }

    @Transactional
    @Override
    public UserResult.TeacherJoin 강사회원가입(UserCommand.TeacherJoin command) {
        Optional<User> userOP = findUserPort.findByUsername(command.username());
        if (userOP.isPresent()) {
            throw new Exception400("중복된 유저네임입니다.");
        }

        User newUser = User.from(command);
        User savedUser = saveUserPort.save(newUser);

        return new UserResult.TeacherJoin(savedUser);
    }
}