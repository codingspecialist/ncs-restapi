package shop.mtcoding.blog.web.user;

import lombok.Data;
import shop.mtcoding.blog.domain.user.User;

public class UserResponse {

    @Data
    public static class DTO {
        private Long id;
        private String username;
        private String email;

        public DTO(User user) {
            this.id = user.getId();
            this.username = user.getUsername();
            this.email = user.getEmail();
        }
    }
}
