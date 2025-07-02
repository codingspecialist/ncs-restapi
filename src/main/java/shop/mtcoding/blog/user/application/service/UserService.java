package shop.mtcoding.blog.user.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.mtcoding.blog._core.errors.exception.api.Exception400;
import shop.mtcoding.blog._core.errors.exception.api.Exception401;
import shop.mtcoding.blog._core.utils.JwtUtil;
import shop.mtcoding.blog._core.utils.MyUtil;
import shop.mtcoding.blog.course.domain.Course;
import shop.mtcoding.blog.user.application.port.in.EmpUseCase;
import shop.mtcoding.blog.user.application.port.in.StudentUseCase;
import shop.mtcoding.blog.user.application.port.in.TeacherUseCase;
import shop.mtcoding.blog.user.application.port.in.UserUseCase;
import shop.mtcoding.blog.user.application.port.in.dto.UserCommand;
import shop.mtcoding.blog.user.application.port.in.dto.UserOutput;
import shop.mtcoding.blog.user.application.port.out.CourseLoadPort;
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
    private final CourseLoadPort courseLoadPort;

    @Override
    public UserOutput.Login 로그인(UserCommand.Login command) {
        User user = userRepositoryPort.findByUsernameAndPassword(
                        command.username(), command.password())
                .orElseThrow(() -> new Exception401("인증되지 않았습니다"));

        String accessToken = JwtUtil.create(user);
        String refreshToken = JwtUtil.createRefresh(user);

        return new UserOutput.Login(user, accessToken, refreshToken);
    }

    @Transactional
    @Override
    public User 학생회원가입(UserCommand.StudentJoin command) {
        Optional<User> userOP = userRepositoryPort.findByUsername(command.username());
        if (userOP.isPresent())
            throw new Exception400("중복된 유저네임입니다.");

        // 다른 도메인 포트를 통해서 가져옴
        Course course = courseLoadPort.loadCourse(command.courseId());
        String authCode = MyUtil.generateAuthCode();

        User savedUser = userRepositoryPort.save(User.createStudent(command, course, authCode));

        // External Port Mocking
        sendEmailPort.sendEmail(savedUser.getEmail(), "회원가입이 완료메시지", "인증코드 : " + authCode);

        return savedUser;
    }

    @Transactional
    @Override
    public User 강사회원가입(UserCommand.TeacherJoin command) {
        Optional<User> userOP = userRepositoryPort.findByUsername(command.username());
        if (userOP.isPresent())
            throw new Exception400("중복된 유저네임입니다.");


        return userRepositoryPort.save(User.createTeacher(command));
    }

    @Transactional
    @Override
    public User 직원회원가입(UserCommand.EmpJoin command) {
        Optional<User> userOP = userRepositoryPort.findByUsername(command.username());
        if (userOP.isPresent())
            throw new Exception400("중복된 유저네임입니다.");


        return userRepositoryPort.save(User.createEmp(command));
    }
}