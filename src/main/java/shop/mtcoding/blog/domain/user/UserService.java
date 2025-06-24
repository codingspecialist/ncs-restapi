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
import shop.mtcoding.blog.domain.user.teacher.Teacher;
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

    public SessionUser 로그인(UserRequest.LoginDTO reqDTO) {
        User userET = userRepository.findByUsernameAndPassword(reqDTO.getUsername(), reqDTO.getPassword())
                .orElseThrow(() -> new Exception401("인증되지 않았습니다"));

        String accessToken = JwtUtil.create(userET);
        String refreshToken = JwtUtil.createRefresh(userET);

        return new SessionUser(userET.getId(), userET.getRole(), accessToken, refreshToken);
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
        if (userOP.isPresent()) throw new Exception400("중복된 유저네임입니다");

        // 2. 인증번호로 강사 조회 (두 줄이 나올 수 없다. 인증번호는 유니크이다)
        Student student = studentRepository.findByAuthCode(reqDTO.getAuthCode())
                .orElseThrow(() -> new Exception403("인증번호가 정확하지 않습니다. 강사에게 문의하세요"));

        // 3. 인증번호의 학생 정보와 회원가입시 들어오는 정보 비교
        Boolean isSameInfo = student.checkNameAndBirthday(reqDTO.getName(), reqDTO.getBirthday());
        if (!isSameInfo) throw new Exception400("인증된 학생의 정보가 아닙니다. 생년월일/이름을 확인하세요.");

        // 4. 유저 회원가입
        User userPS = userRepository.save(reqDTO.toEntity());

        // 5. 학생 인증 완료 (더티체킹)
        student.setVerified(userPS);

        // 6. 세션 동기화를 위해 user에 값 채워주기 sync
        userPS.setStudent(student);

        // user select 쳐야함 (student join해서)

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
