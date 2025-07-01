package shop.mtcoding.blog.domain.user.application.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import shop.mtcoding.blog._core.errors.exception.api.Exception400;
import shop.mtcoding.blog._core.errors.exception.api.Exception401;
import shop.mtcoding.blog._core.errors.exception.api.Exception404;
import shop.mtcoding.blog._core.utils.JwtUtil;
import shop.mtcoding.blog._core.utils.MyUtil;
import shop.mtcoding.blog.domain.course.application.port.out.FindCoursePort;
import shop.mtcoding.blog.domain.course.model.Course;
import shop.mtcoding.blog.domain.user.application.dto.UserCommand;
import shop.mtcoding.blog.domain.user.application.dto.UserResult;
import shop.mtcoding.blog.domain.user.application.port.out.FindUserPort;
import shop.mtcoding.blog.domain.user.application.port.out.SaveUserPort;
import shop.mtcoding.blog.domain.user.model.Student;
import shop.mtcoding.blog.domain.user.model.Teacher;
import shop.mtcoding.blog.domain.user.model.User;
import shop.mtcoding.blog.domain.user.model.UserType;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

/**
 * UserService의 도메인 로직을 테스트하는 클래스입니다.
 * 외부 의존성(FindUserPort, SaveUserPort 등)은 Mocking하여
 * UserService 자체의 비즈니스 로직에만 집중하여 테스트합니다.
 * <p>
 * **주의:** 현재 UserService 코드에 PasswordEncoder가 없으므로, 비밀번호 암호화는 테스트 범위에 포함되지 않습니다.
 * 실제 서비스에서는 반드시 비밀번호 암호화를 적용해야 합니다.
 * </p>
 */
@ExtendWith(MockitoExtension.class) // Mockito를 JUnit 5와 통합
public class UserServiceTest {

    @InjectMocks // 테스트 대상이 되는 UserService 인스턴스를 주입 (Mock 객체들이 여기에 주입됨)
    private UserService userService;

    @Mock // Mock 객체로 만들 의존성들
    private FindUserPort findUserPort;
    @Mock
    private SaveUserPort saveUserPort;
    @Mock
    private FindCoursePort findCoursePort;

    // 각 테스트 메서드 실행 전에 초기화 (현재는 PasswordEncoder가 없으므로 비어있음)
    @BeforeEach
    void setUp() {
        // PasswordEncoder가 없으므로 관련 Mocking 없음
    }

    @DisplayName("강사 회원가입 성공 테스트")
    @Test
    void teacherJoin_success_test() {
        // given (테스트에 필요한 입력 데이터 및 Mock 객체의 동작 설정)
        UserCommand.TeacherJoin command = new UserCommand.TeacherJoin(
                "teacher123", "plainPassword", "teacher@example.com", "김강사", UserType.TEACHER, "사인내용"
        );

        // 1. findUserPort.findByUsername() 호출 시, Optional.empty()를 반환하여 중복이 없음을 가정
        when(findUserPort.findByUsername(command.username())).thenReturn(Optional.empty());

        // 2. saveUserPort.save()가 반환할 User 객체를 미리 정의
        // User.from(command)가 반환할 객체와 동일하게 구성 (User.java의 from 메서드와 일치해야 함)
        User mockUser = User.builder()
                .id(1L)
                .username(command.username())
                .password(command.password()) // 현재 코드 기준: 암호화되지 않은 비밀번호
                .email(command.email())
                .role(UserType.TEACHER)
                .teacher(Teacher.builder().name(command.name()).sign(command.sign()).build())
                .build();
        // User.from() 내부에서 teacher.setUser(user)가 호출될 것이므로, Teacher 객체에도 User를 설정
        if (mockUser.getTeacher() != null) { // null 체크 추가 (Teacher가 null일 경우 대비)
            mockUser.getTeacher().setUser(mockUser);
        }

        // 3. saveUserPort.save() 호출 시, 위에서 정의한 mockUser를 반환하도록 설정
        when(saveUserPort.save(any(User.class))).thenReturn(mockUser);

        // when (테스트 대상 메서드 실행)
        UserResult.TeacherJoin result = userService.강사회원가입(command);

        // then (결과 검증)
        assertThat(result).isNotNull();
        assertThat(result.user().getUsername()).isEqualTo(command.username());
        assertThat(result.user().getEmail()).isEqualTo(command.email());
        assertThat(result.user().getRole()).isEqualTo(UserType.TEACHER);
        assertThat(result.user().getTeacher()).isNotNull();
        assertThat(result.user().getTeacher().getName()).isEqualTo(command.name());
        assertThat(result.user().getTeacher().getSign()).isEqualTo(command.sign());
        assertThat(result.user().getPassword()).isEqualTo(command.password()); // 암호화되지 않은 비밀번호 확인
    }

    @DisplayName("강사 회원가입 실패 테스트 - 유저네임 중복")
    @Test
    void teacherJoin_duplicateUsername_fail_test() {
        // given
        UserCommand.TeacherJoin command = new UserCommand.TeacherJoin(
                "teacher123", "plainPassword", "teacher@example.com", "김강사", UserType.TEACHER, "사인내용"
        );

        // findUserPort.findByUsername() 호출 시, 이미 존재하는 User를 반환하도록 설정하여 중복 상황 가정
        when(findUserPort.findByUsername(command.username()))
                .thenReturn(Optional.of(User.builder().username(command.username()).build())); // 이미 존재하는 유저 반환

        // when & then (예외 발생 검증)
        // Exception400 예외가 발생하는지 확인
        Exception400 e = assertThrows(Exception400.class, () -> {
            userService.강사회원가입(command);
        });

        // 예외 메시지 검증
        assertThat(e.getMessage()).isEqualTo("중복된 유저네임입니다.");
    }

    @DisplayName("학생 회원가입 성공 테스트")
    @Test
    void studentJoin_success_test() {
        // given
        UserCommand.StudentJoin command = new UserCommand.StudentJoin(
                "student123", "plainPassword", "student@example.com", "박학생", UserType.STUDENT, 1L, "2000-01-01"
        );
        Course mockCourse = Course.builder().id(1L).title("Java 기초").build();
        String mockAuthCode = "ABCDEF";

        // 1. findUserPort.findByUsername() 호출 시, Optional.empty()를 반환하여 중복이 없음을 가정
        when(findUserPort.findByUsername(command.username())).thenReturn(Optional.empty());

        // 2. findCoursePort.findById() 호출 시, mockCourse 반환
        when(findCoursePort.findById(command.courseId())).thenReturn(Optional.of(mockCourse));

        // MyUtil.generateAuthCode()는 static 메서드이므로 mockStatic으로 처리
        try (MockedStatic<MyUtil> mockedMyUtil = mockStatic(MyUtil.class)) {
            mockedMyUtil.when(MyUtil::generateAuthCode).thenReturn(mockAuthCode);

            // 3. saveUserPort.save()가 반환할 User 객체 정의
            // User.from(command, course, authCode)가 반환할 객체와 동일하게 구성
            User mockUser = User.builder()
                    .id(2L)
                    .username(command.username())
                    .password(command.password()) // 현재 코드 기준: 암호화되지 않은 비밀번호
                    .email(command.email())
                    .role(UserType.STUDENT)
                    .student(Student.builder()
                            .name(command.name())
                            .birthday(command.birthday())
                            .course(mockCourse)
                            .authCode(mockAuthCode)
                            .isVerified(false)
                            .build())
                    .build();
            // User.from() 내부에서 student.setUser(user)가 호출될 것이므로, Student 객체에도 User를 설정
            if (mockUser.getStudent() != null) { // null 체크 추가
                mockUser.getStudent().setUser(mockUser);
            }

            // 4. saveUserPort.save() 호출 시, 위에서 정의한 mockUser를 반환하도록 설정
            when(saveUserPort.save(any(User.class))).thenReturn(mockUser);

            // when
            UserResult.StudentJoin result = userService.학생회원가입(command);

            // then
            assertThat(result).isNotNull();
            assertThat(result.user().getUsername()).isEqualTo(command.username());
            assertThat(result.user().getEmail()).isEqualTo(command.email());
            assertThat(result.user().getRole()).isEqualTo(UserType.STUDENT);
            assertThat(result.user().getStudent()).isNotNull();
            assertThat(result.user().getStudent().getName()).isEqualTo(command.name());
            assertThat(result.user().getStudent().getCourse().getTitle()).isEqualTo(mockCourse.getTitle());
            assertThat(result.user().getStudent().getAuthCode()).isEqualTo(mockAuthCode);
            assertThat(result.user().getPassword()).isEqualTo(command.password()); // 암호화되지 않은 비밀번호 확인
        }
    }

    @DisplayName("학생 회원가입 실패 테스트 - 유저네임 중복")
    @Test
    void studentJoin_duplicateUsername_fail_test() {
        // given
        UserCommand.StudentJoin command = new UserCommand.StudentJoin(
                "student123", "plainPassword", "student@example.com", "박학생", UserType.STUDENT, 1L, "2000-01-01"
        );

        when(findUserPort.findByUsername(command.username())).thenReturn(
                Optional.of(User.builder().username(command.username()).build()));

        // when & then
        Exception400 e = assertThrows(Exception400.class, () -> {
            userService.학생회원가입(command);
        });
        assertThat(e.getMessage()).isEqualTo("중복된 유저네임입니다.");
    }

    @DisplayName("학생 회원가입 실패 테스트 - 과정 없음")
    @Test
    void studentJoin_courseNotFound_fail_test() {
        // given
        UserCommand.StudentJoin command = new UserCommand.StudentJoin(
                "student123", "plainPassword", "student@example.com", "박학생", UserType.STUDENT, 999L, "2000-01-01"
        );

        when(findUserPort.findByUsername(command.username())).thenReturn(Optional.empty());
        when(findCoursePort.findById(command.courseId())).thenReturn(Optional.empty());

        // when & then
        Exception404 e = assertThrows(Exception404.class, () -> {
            userService.학생회원가입(command);
        });
        assertThat(e.getMessage()).isEqualTo("조회된 과정이 없습니다.");
    }

    @DisplayName("로그인 성공 테스트")
    @Test
    void login_success_test() {
        // given
        UserCommand.Login loginCommand = new UserCommand.Login("testuser", "testpass");

        // Mock User 객체 생성 (실제 DB에서 조회될 User를 가정)
        User mockUser = User.builder()
                .id(1L)
                .username("testuser")
                .password("testpass") // 현재 코드 기준: 암호화되지 않은 비밀번호
                .email("test@example.com")
                .role(UserType.STUDENT)
                .build();

        // findUserPort.findByUsernameAndPassword() 호출 시 mockUser를 반환하도록 설정
        when(findUserPort.findByUsernameAndPassword(loginCommand.username(), loginCommand.password()))
                .thenReturn(Optional.of(mockUser));

        // JwtUtil.create() 및 JwtUtil.createRefresh()는 static 메서드이므로 mockStatic으로 처리
        try (MockedStatic<JwtUtil> mockedJwtUtil = mockStatic(JwtUtil.class)) {
            mockedJwtUtil.when(() -> JwtUtil.create(any(User.class))).thenReturn("mockAccessToken");
            mockedJwtUtil.when(() -> JwtUtil.createRefresh(any(User.class))).thenReturn("mockRefreshToken");

            // when
            UserResult.Login result = userService.로그인(loginCommand);

            // then
            assertThat(result).isNotNull();
            assertThat(result.user().getUsername()).isEqualTo("testuser");
            assertThat(result.accessToken()).isEqualTo("mockAccessToken");
            assertThat(result.refreshToken()).isEqualTo("mockRefreshToken");
            assertThat(result.user().getPassword()).isEqualTo("testpass"); // 현재 코드 기준: 암호화되지 않은 비밀번호 확인
        }
    }

    @DisplayName("로그인 실패 테스트 - 인증되지 않음")
    @Test
    void login_fail_authentication_test() {
        // given
        UserCommand.Login loginCommand = new UserCommand.Login("wronguser", "wrongpass");

        // findUserPort.findByUsernameAndPassword() 호출 시 Optional.empty()를 반환하도록 설정
        when(findUserPort.findByUsernameAndPassword(loginCommand.username(), loginCommand.password()))
                .thenReturn(Optional.empty());

        // when & then
        Exception401 e = assertThrows(Exception401.class, () -> {
            userService.로그인(loginCommand);
        });

        assertThat(e.getMessage()).isEqualTo("인증되지 않았습니다");
    }
}