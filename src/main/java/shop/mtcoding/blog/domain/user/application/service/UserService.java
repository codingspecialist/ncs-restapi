package shop.mtcoding.blog.domain.user.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.mtcoding.blog._core.errors.exception.api.Exception400;
import shop.mtcoding.blog._core.errors.exception.api.Exception401;
import shop.mtcoding.blog._core.errors.exception.api.Exception404;
import shop.mtcoding.blog._core.utils.JwtUtil;
import shop.mtcoding.blog._core.utils.MyUtil;
import shop.mtcoding.blog.course.model.Course;
import shop.mtcoding.blog.course.port.out.CourseRepositoryPort;
import shop.mtcoding.blog.domain.user.application.port.in.EmpUseCase;
import shop.mtcoding.blog.domain.user.application.port.in.StudentUseCase;
import shop.mtcoding.blog.domain.user.application.port.in.TeacherUseCase;
import shop.mtcoding.blog.domain.user.application.port.in.UserUseCase;
import shop.mtcoding.blog.domain.user.application.port.in.dto.UserCommand;
import shop.mtcoding.blog.domain.user.application.port.in.dto.UserOutput;
import shop.mtcoding.blog.domain.user.application.port.out.UserRepositoryPort;
import shop.mtcoding.blog.domain.user.domain.User;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements
        UserUseCase, StudentUseCase, TeacherUseCase, EmpUseCase {

    private final UserRepositoryPort userRepository;
    private final CourseRepositoryPort courseRepository;

    @Override
    public UserOutput.SessionItem 로그인(UserCommand.Login command) {
        User user = userRepository.findByUsernameAndPassword(
                        command.username(), command.password())
                .orElseThrow(() -> new Exception401("인증되지 않았습니다"));

        String accessToken = JwtUtil.create(user);
        String refreshToken = JwtUtil.createRefresh(user);

        return new UserOutput.SessionItem(user, accessToken, refreshToken);
    }

    @Transactional
    @Override
    public User 학생회원가입(UserCommand.StudentJoin command) {
        Optional<User> userOP = userRepository.findByUsername(command.username());
        if (userOP.isPresent())
            throw new Exception400("중복된 유저네임입니다.");


        Course course = courseRepository.findById(command.courseId())
                .orElseThrow(() -> new Exception404("조회된 과정이 없습니다."));
        String authCode = MyUtil.generateAuthCode();

        return userRepository.save(User.from(command, course, authCode));
    }

    @Transactional
    @Override
    public User 강사회원가입(UserCommand.TeacherJoin command) {
        Optional<User> userOP = userRepository.findByUsername(command.username());
        if (userOP.isPresent())
            throw new Exception400("중복된 유저네임입니다.");


        return userRepository.save(User.from(command));
    }

    @Transactional
    @Override
    public User 직원회원가입(UserCommand.EmpJoin command) {
        Optional<User> userOP = userRepository.findByUsername(command.username());
        if (userOP.isPresent())
            throw new Exception400("중복된 유저네임입니다.");


        return userRepository.save(User.from(command));
    }
}