package shop.mtcoding.blog.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.mtcoding.blog._core.errors.exception.Exception400;
import shop.mtcoding.blog._core.errors.exception.Exception401;
import shop.mtcoding.blog._core.errors.exception.Exception403;
import shop.mtcoding.blog._core.errors.exception.Exception500;
import shop.mtcoding.blog.user.student.Student;
import shop.mtcoding.blog.user.student.StudentRepository;
import shop.mtcoding.blog.user.teacher.Teacher;

import java.util.Optional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service // IoC 등록
public class UserService {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;

    public User 로그인(UserRequest.LoginDTO reqDTO) {
        User sessionUser = userRepository.findByUsernameAndPassword(reqDTO.getUsername(), reqDTO.getPassword())
                .orElseThrow(() -> new Exception401("인증되지 않았습니다"));
        return sessionUser; // student 정보 포함
    }

    @Transactional
    public User 회원가입(UserRequest.JoinDTO reqDTO) {
        // 1. 유저네임 중복검사
        Optional<User> userOP = userRepository.findByUsername(reqDTO.getUsername());

        if (userOP.isPresent()) {
            throw new Exception400("중복된 유저네임입니다");
        }

        // 2. 학생 or 선생 회원가입
        User userPS;
        if (UserEnum.valueOf(reqDTO.getRole()) == UserEnum.STUDENT) {
            Student student = studentRepository.findByAuthCode(reqDTO.getAuthCode())
                    .orElseThrow(() -> new Exception403("학생 인증이 실패하였습니다"));
            userPS = userRepository.findById(student.getUser().getId())
                    .orElseThrow(() -> new Exception500("학생으로 등록되어 있는데 유저를 찾을 수 없습니다. 관리자에게 문의하세요"));
            userPS.authentication(reqDTO.getUsername(), reqDTO.getPassword(), reqDTO.getEmail(), UserEnum.STUDENT);
            student.setVerified(true);
        } else {
            // 2. 회원가입 (Save or Update) - update는 학생일때만!
            userPS = userRepository.save(reqDTO.toEntity());

            // 3. 학생이면 학생을 선등록해야 하고, 선생님이면 회원가입시 동시 저장
            if (userPS.getRole() == UserEnum.TEACHER) {
                Teacher teacher = Teacher.builder()
                        .user(userPS)
                        .build();
                userPS.setTeacher(teacher); // 캐스캐이드 저장
            }
        }


        return userPS;
    }

    // TODO: 사인저장 완료해야함
    @Transactional
    public void 사인저장(UserRequest.TeacherSignDTO reqDTO, User sessionUser) {
        User userPS = userRepository.findById(sessionUser.getId())
                .orElseThrow(() -> new Exception400("잘못된 요청입니다. 없는 회원이에요."));
        userPS.getStudent().setVerified(true);
    } // 더티체킹
}
