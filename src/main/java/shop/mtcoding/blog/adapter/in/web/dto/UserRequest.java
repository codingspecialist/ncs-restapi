package shop.mtcoding.blog.adapter.in.web.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import shop.mtcoding.blog.domain.user.application.dto.UserCommand;
import shop.mtcoding.blog.domain.user.model.UserType;

/**
 * 웹 계층에서 사용자의 요청을 받는 DTO들을 정의합니다.
 * 각 DTO는 유효성 검증 로직과 자신을 Application 계층의 Command 객체로 변환하는 책임을 가집니다.
 */
public class UserRequest {

    /**
     * 교사 회원가입을 위한 웹 요청 DTO
     */
    public record TeacherJoin(
            @NotEmpty(message = "유저네임은 공백일 수 없습니다")
            @Size(min = 3, max = 20, message = "유저네임은 3자에서 20자 사이여야 합니다")
            String username,

            @NotEmpty(message = "비밀번호는 공백일 수 없습니다")
            @Size(min = 4, max = 20, message = "비밀번호는 4자에서 20자 사이여야 합니다")
            String password,

            @NotEmpty(message = "이메일은 공백일 수 없습니다")
            @Pattern(regexp = "^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$", message = "유효한 이메일 주소를 입력해주세요")
            String email,

            @NotEmpty(message = "이름은 공백일 수 없습니다")
            String name,

            String sign // 서명은 선택사항일 수 있으므로 유효성 검증 제외
    ) {
        /**
         * 웹 요청 DTO를 서비스 계층에서 사용할 Command 객체로 변환합니다.
         *
         * @return TeacherJoinCommand
         */
        public UserCommand.TeacherJoin toCommand() {
            return new UserCommand.TeacherJoin(
                    this.username,
                    this.password,
                    this.email,
                    this.name,
                    UserType.TEACHER, // 역할(Role)은 이 단계에서 확정
                    this.sign
            );
        }
    }

    /**
     * 학생 회원가입을 위한 웹 요청 DTO
     */
    public record StudentJoin(
            @NotEmpty(message = "유저네임은 공백일 수 없습니다")
            @Size(min = 3, max = 20, message = "유저네임은 3자에서 20자 사이여야 합니다")
            String username,

            @NotEmpty(message = "비밀번호는 공백일 수 없습니다")
            @Size(min = 4, max = 20, message = "비밀번호는 4자에서 20자 사이여야 합니다")
            String password,

            @NotEmpty(message = "이메일은 공백일 수 없습니다")
            @Pattern(regexp = "^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$", message = "유효한 이메일 주소를 입력해주세요")
            String email,

            @NotEmpty(message = "이름은 공백일 수 없습니다")
            String name,

            @NotNull(message = "과정 ID는 공백일 수 없습니다")
            Long courseId,

            @NotEmpty(message = "생년월일은 공백일 수 없습니다")
            String birthday
    ) {
        /**
         * 웹 요청 DTO를 서비스 계층에서 사용할 Command 객체로 변환합니다.
         *
         * @return StudentJoinCommand
         */
        public UserCommand.StudentJoin toCommand() {
            return new UserCommand.StudentJoin(
                    this.username,
                    this.password,
                    this.email,
                    this.name,
                    UserType.STUDENT,
                    this.courseId,
                    this.birthday
            );
        }
    }

    /**
     * 로그인을 위한 웹 요청 DTO
     */
    public record Login(
            @NotEmpty(message = "유저네임은 공백일 수 없습니다")
            String username,

            @NotEmpty(message = "비밀번호는 공백일 수 없습니다")
            String password
    ) {
        /**
         * 웹 요청 DTO를 서비스 계층에서 사용할 Command 객체로 변환합니다.
         *
         * @return LoginCommand
         */
        public UserCommand.Login toCommand() {
            return new UserCommand.Login(this.username, this.password);
        }
    }
}