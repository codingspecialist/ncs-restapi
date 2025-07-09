package shop.mtcoding.blog.user.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.mtcoding.blog._core.errors.exception.api.Exception400;
import shop.mtcoding.blog._core.errors.exception.api.Exception401;
import shop.mtcoding.blog._core.utils.JwtUtil;
import shop.mtcoding.blog._core.utils.MyUtil;
import shop.mtcoding.blog.user.application.port.in.EmpUseCase;
import shop.mtcoding.blog.user.application.port.in.StudentUseCase;
import shop.mtcoding.blog.user.application.port.in.TeacherUseCase;
import shop.mtcoding.blog.user.application.port.in.UserUseCase;
import shop.mtcoding.blog.user.application.port.in.dto.UserCommand;
import shop.mtcoding.blog.user.application.port.in.dto.UserOutput;
import shop.mtcoding.blog.user.application.port.out.SendEmailPort;
import shop.mtcoding.blog.user.application.port.out.UserRepositoryPort;
import shop.mtcoding.blog.user.domain.User;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements
        UserUseCase, StudentUseCase, TeacherUseCase, EmpUseCase {

    private final UserRepositoryPort userRepositoryPort;
    private final SendEmailPort sendEmailPort;

    @Override
    public UserOutput.Session 로그인(UserCommand.Login command) {
        User findUser = userRepositoryPort.findByUsernameAndPassword(
                        command.username(), command.password())
                .orElseThrow(() -> new Exception401("인증되지 않았습니다"));

        String accessToken = JwtUtil.create(findUser);
        String refreshToken = JwtUtil.createRefresh(findUser);

        return new UserOutput.Session(findUser, accessToken, refreshToken);
    }

    @Transactional
    @Override
    public UserOutput.Max 학생회원가입(UserCommand.StudentJoin command) {
        Optional<User> userOP = userRepositoryPort.findByUsername(command.username());
        if (userOP.isPresent())
            throw new Exception400("중복된 유저네임입니다.");

        String authCode = MyUtil.generateAuthCode();
        User savedUser = userRepositoryPort.save(User.createStudent(command, authCode));

        sendEmailPort.sendEmail(savedUser.getEmail(), "회원가입이 완료메시지", "인증코드 : " + authCode);

        return new UserOutput.Max(savedUser);
    }

    @Transactional
    @Override
    public UserOutput.Max 강사회원가입(UserCommand.TeacherJoin command) {
        Optional<User> userOP = userRepositoryPort.findByUsername(command.username());
        if (userOP.isPresent())
            throw new Exception400("중복된 유저네임입니다.");

        User savedUser = userRepositoryPort.save(User.createTeacher(command));
        return new UserOutput.Max(savedUser);
    }

    @Transactional
    @Override
    public UserOutput.Max 직원회원가입(UserCommand.EmpJoin command) {
        Optional<User> userOP = userRepositoryPort.findByUsername(command.username());
        if (userOP.isPresent())
            throw new Exception400("중복된 유저네임입니다.");
        User savedUser = userRepositoryPort.save(User.createEmp(command));

        return new UserOutput.Max(savedUser);
    }
}