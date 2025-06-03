package shop.mtcoding.blog.web.user;

import lombok.Data;
import shop.mtcoding.blog.domain.user.User;
import shop.mtcoding.blog.domain.user.UserType;

public class UserRequest {

    @Data
    public static class TeacherSignDTO {
        private String sign;
    }

    @Data
    public static class JoinDTO {
        private String username; // 3~20 자 사이로 받아야 함
        private String password;
        private String email;
        private String name;
        private String role;
        private String authCode; // 학생일때만 받음
        private String birthday; // 학생일때만 받음

        public User toEntity() {
            return User.builder()
                    .username(username)
                    .password(password)
                    .email(email)
                    .role(UserType.valueOf(role))
                    .build();
        }
    }

    @Data
    public static class LoginDTO {
        private String username;
        private String password;
    }
}
