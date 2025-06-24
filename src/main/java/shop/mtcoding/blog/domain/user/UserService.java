package shop.mtcoding.blog.domain.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.mtcoding.blog.core.errors.exception.api.Exception400;
import shop.mtcoding.blog.core.errors.exception.api.Exception401;
import shop.mtcoding.blog.core.errors.exception.api.Exception403;
import shop.mtcoding.blog.core.utils.JwtUtil;
import shop.mtcoding.blog.domain.user.student.Student;
import shop.mtcoding.blog.domain.user.student.StudentRepository;
import shop.mtcoding.blog.domain.user.teacher.TeacherRepository;
import shop.mtcoding.blog.web.user.UserRequest;

import java.util.Optional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service // IoC 등록
public class UserService {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;

    // DTO로 응답했어야 했네!!
    public SessionUser 로그인(UserRequest.LoginDTO reqDTO) {
        User userET = userRepository.findByUsernameAndPassword(reqDTO.getUsername(), reqDTO.getPassword())
                .orElseThrow(() -> new Exception401("인증되지 않았습니다"));

        String accessToken = JwtUtil.create(userET);
        String refreshToken = JwtUtil.createRefresh(userET);

        return new SessionUser(userET.getId(), userET.getRole(), accessToken, refreshToken);
    }


    @Transactional
    public User 강사_회원가입(UserRequest.TeacherJoin reqDTO) {
        // 1. 유저네임 중복검사
        Optional<User> userOP = userRepository.findByUsername(reqDTO.getUsername());

        if (userOP.isPresent()) {
            throw new Exception400("중복된 유저네임입니다");
        }

        // 2. 회원가입 (User, Teacher)
        User userET = userRepository.save(reqDTO.toEntity());
        return userET;
    }

    // 인증코드로 인증필요
    @Transactional
    public User 학생_회원가입(UserRequest.StudentJoin reqDTO) {
        // 1. 유저네임 중복검사
        Optional<User> userOP = userRepository.findByUsername(reqDTO.getUsername());
        if (userOP.isPresent()) throw new Exception400("중복된 유저네임입니다");

        // 2. 인증번호로 강사 조회 (두 줄이 나올 수 없다. 인증번호는 유니크이다)
        Student student = studentRepository.findByAuthCode(reqDTO.getAuthCode())
                .orElseThrow(() -> new Exception403("인증번호가 정확하지 않습니다. 강사에게 문의하세요"));

        // 3. 유저 회원가입
        User userPS = userRepository.save(reqDTO.toEntity());

        return userPS;
    }
}
