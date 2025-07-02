package shop.mtcoding.blog.user.application.port.in.dto;

import shop.mtcoding.blog.user.adapter.in.web.dto.UserRequest;
import shop.mtcoding.blog.user.domain.enums.UserRole;

public class UserCommand {

    public record TeacherJoin(String username, String password, String email, String name, UserRole role, String sign) {
        public static TeacherJoin from(UserRequest.TeacherJoin request) {
            return new TeacherJoin(
                    request.username(),
                    request.password(),
                    request.email(),
                    request.name(),
                    UserRole.TEACHER, // Role is still determined here
                    request.sign()
            );
        }
    }

    public record StudentJoin(String username, String password, String email, String name, UserRole role, Long courseId,
                              String birthday) {
        public static StudentJoin from(UserRequest.StudentJoin request) {
            return new StudentJoin(
                    request.username(),
                    request.password(),
                    request.email(),
                    request.name(),
                    UserRole.STUDENT,
                    request.courseId(),
                    request.birthday()
            );
        }
    }

    public record EmpJoin(String username, String password, String email, String name, UserRole role, String sign) {
        public static EmpJoin from(UserRequest.EmpJoin request) {
            return new EmpJoin(
                    request.username(),
                    request.password(),
                    request.email(),
                    request.name(),
                    UserRole.EMP,
                    request.sign()
            );
        }
    }

    public record Login(String username, String password) {
        public static Login from(UserRequest.Login request) {
            return new Login(request.username(), request.password());
        }
    }
}