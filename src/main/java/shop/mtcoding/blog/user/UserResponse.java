package shop.mtcoding.blog.user;

import lombok.Data;

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
