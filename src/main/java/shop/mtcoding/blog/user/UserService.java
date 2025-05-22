package shop.mtcoding.blog.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.mtcoding.blog._core.errors.exception.Exception400;
import shop.mtcoding.blog._core.errors.exception.Exception401;
import shop.mtcoding.blog._core.errors.exception.Exception403;
import shop.mtcoding.blog._core.errors.exception.Exception500;
import shop.mtcoding.blog.course.student.Student;
import shop.mtcoding.blog.course.student.StudentRepository;
import shop.mtcoding.blog.user.teacher.Teacher;
import shop.mtcoding.blog.user.teacher.TeacherRepository;

import java.util.Optional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service // IoC 등록
public class UserService {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;

    public User 로그인(UserRequest.LoginDTO reqDTO) {
        User userPS = userRepository.findByUsernameAndPassword(reqDTO.getUsername(), reqDTO.getPassword())
                .orElseThrow(() -> new Exception401("인증되지 않았습니다"));
        return userPS; // student, teacher 정보 포함
    }

    @Transactional
    public User 강사회원가입(UserRequest.JoinDTO reqDTO) {
        // 1. 유저네임 중복검사
        Optional<User> userOP = userRepository.findByUsername(reqDTO.getUsername());

        if (userOP.isPresent()) {
            throw new Exception400("중복된 유저네임입니다");
        }

        User userPS = userRepository.save(reqDTO.toEntity());

        // 2. 회원가입
        Teacher teacher = Teacher.builder()
                .user(userPS)
                .name(reqDTO.getName())
                .build();

        teacherRepository.save(teacher);
        return userPS;
    }

    // TODO: 현재사용안함 version 2.0 에 하기
    @Transactional
    public User 직원회원가입(UserRequest.JoinDTO reqDTO) {
        // 1. 유저네임 중복검사
        Optional<User> userOP = userRepository.findByUsername(reqDTO.getUsername());

        if (userOP.isPresent()) {
            throw new Exception400("중복된 유저네임입니다");
        }
        return null;
    }

    @Transactional
    public User 학생회원가입(UserRequest.JoinDTO reqDTO) {
        // 1. 유저네임 중복검사
        Optional<User> userOP = userRepository.findByUsername(reqDTO.getUsername());

        if (userOP.isPresent()) {
            throw new Exception400("중복된 유저네임입니다");
        }

        // 2. 강사가 등록한 학생과 매칭되는지 확인
        Student student = studentRepository.findByAuthCodeAndBirthdayAndIsNotVerified(reqDTO.getAuthCode(), reqDTO.getBirthday())
                .orElseThrow(() -> new Exception403("학생 인증이 실패하였습니다"));

        // 3. 학생 인증 완료 및 업데이트로 회원가입
        User userPS = userRepository.findById(student.getUser().getId())
                .orElseThrow(() -> new Exception500("학생으로 등록되어 있는데 유저를 찾을 수 없는 오류!! 관리자에게 문의하세요"));

        if (!reqDTO.getName().equals(userPS.getStudent().getName())) {
            throw new Exception400("인증된 학생의 이름이 아니에요");
        }

        userPS.studentAuthentication(reqDTO.getUsername(), reqDTO.getPassword(), reqDTO.getEmail(), UserEnum.STUDENT);
        student.setVerified(true);

        return userPS;
    }

    @Transactional
    public User 강사사인저장(UserRequest.TeacherSignDTO reqDTO, User sessionUser) {
        Teacher teacherPS = teacherRepository.findByUserId(sessionUser.getId());
        teacherPS.setSign(reqDTO.getSign());
        sessionUser.setTeacher(teacherPS);
        return sessionUser;
    }
}
