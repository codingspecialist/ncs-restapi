package shop.mtcoding.blog.domain.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.mtcoding.blog.core.errors.exception.api.Exception400;
import shop.mtcoding.blog.core.errors.exception.api.Exception401;
import shop.mtcoding.blog.core.errors.exception.api.Exception404;
import shop.mtcoding.blog.core.utils.JwtUtil;
import shop.mtcoding.blog.core.utils.MyUtil;
import shop.mtcoding.blog.domain.course.Course;
import shop.mtcoding.blog.domain.course.CourseRepository;
import shop.mtcoding.blog.domain.user.student.StudentRepository;
import shop.mtcoding.blog.domain.user.teacher.TeacherRepository;
import shop.mtcoding.blog.web.user.UserRequest;

import java.util.Optional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;

    public UserModel.Item 로그인(UserRequest.LoginDTO reqDTO) {
        // 1. 인증 확인
        User userET = userRepository.findByUsernameAndPassword(reqDTO.getUsername(), reqDTO.getPassword())
                .orElseThrow(() -> new Exception401("인증되지 않았습니다"));
        // 2. 토큰생성
        String accessToken = JwtUtil.create(userET);
        String refreshToken = JwtUtil.createRefresh(userET);
        return new UserModel.Item(userET, accessToken, refreshToken);
    }

    // 강사 사인 받아야함
    @Transactional
    public UserModel.Item 강사_회원가입(UserRequest.TeacherJoin reqDTO) {
        // 1. 유저네임 중복검사
        Optional<User> userOP = userRepository.findByUsername(reqDTO.getUsername());
        if (userOP.isPresent()) throw new Exception400("중복된 유저네임입니다");

        // 2. 회원가입 (User, Teacher)
        User userET = userRepository.save(reqDTO.toEntity());
        return new UserModel.Item(userET, null, null);
    }

    // 인증코드로 인증필요
    @Transactional
    public UserModel.Item 학생_회원가입(UserRequest.StudentJoin reqDTO) {
        // 1. 유저네임 중복검사
        Optional<User> userOP = userRepository.findByUsername(reqDTO.getUsername());
        if (userOP.isPresent()) throw new Exception400("중복된 유저네임입니다");

        // 2. 과정 조회
        Course courseET = courseRepository.findById(reqDTO.getCourseId())
                .orElseThrow(() -> new Exception404("조회된 과정이 없습니다"));

        // 3. 인증번호 생성
        String authCode = MyUtil.generateAuthCode();

        // 4. 회원가입 (User, Student)
        User userET = userRepository.save(reqDTO.toEntity(courseET, authCode));
        return new UserModel.Item(userET, null, null);
    }
}
