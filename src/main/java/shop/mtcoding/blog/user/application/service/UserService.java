package shop.mtcoding.blog.user.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.mtcoding.blog._core.errors.exception.api.Exception400;
import shop.mtcoding.blog._core.errors.exception.api.Exception401;
import shop.mtcoding.blog._core.utils.JwtUtil;
import shop.mtcoding.blog._core.utils.MyUtil;
import shop.mtcoding.blog.user.application.domain.User;
import shop.mtcoding.blog.user.application.repository.UserRepository;
import shop.mtcoding.blog.user.application.service.dto.UserCommand;
import shop.mtcoding.blog.user.application.service.dto.UserOutput;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserOutput.Session 로그인(UserCommand.Login command) {
        User findUser = userRepository.findByUsernameAndPassword(
                        command.username(), command.password())
                .orElseThrow(() -> new Exception401("인증되지 않았습니다"));

        String accessToken = JwtUtil.create(findUser);
        String refreshToken = JwtUtil.createRefresh(findUser);

        return new UserOutput.Session(findUser, accessToken, refreshToken);
    }

    @Transactional
    public UserOutput.Max 학생회원가입(UserCommand.StudentJoin command) {
        Optional<User> userOP = userRepository.findByUsername(command.username());
        if (userOP.isPresent())
            throw new Exception400("중복된 유저네임입니다.");

        String authCode = MyUtil.generateAuthCode();
        User savedUser = userRepository.save(User.createStudent(command, authCode));

        return new UserOutput.Max(savedUser);
    }

    @Transactional
    public UserOutput.Max 강사회원가입(UserCommand.TeacherJoin command) {
        Optional<User> userOP = userRepository.findByUsername(command.username());
        if (userOP.isPresent())
            throw new Exception400("중복된 유저네임입니다.");

        User savedUser = userRepository.save(User.createTeacher(command));
        return new UserOutput.Max(savedUser);
    }

    @Transactional
    public UserOutput.Max 직원회원가입(UserCommand.EmpJoin command) {
        Optional<User> userOP = userRepository.findByUsername(command.username());
        if (userOP.isPresent())
            throw new Exception400("중복된 유저네임입니다.");
        User savedUser = userRepository.save(User.createEmp(command));

        return new UserOutput.Max(savedUser);
    }
}