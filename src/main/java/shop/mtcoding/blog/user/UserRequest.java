package shop.mtcoding.blog.user;

import lombok.Data;

public class UserRequest {

    @Data
    public static class TeacherSignDTO {
        private String sign;
    }

    @Data
    public static class JoinDTO {
        private String username;
        private String password;
        private String email;
        private String name;
        private String role;
        private String authCode; // null 가능
        private String birthday; // null 가능

        public User toEntity() {
            return User.builder()
                    .username(username)
                    .password(password)
                    .email(email)
                    .name(name)
                    .role(UserEnum.valueOf(role))
                    .build();
        }
    }

    @Data
    public static class LoginDTO {
        private String username;
        private String password;
    }
}
