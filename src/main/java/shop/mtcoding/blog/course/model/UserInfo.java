package shop.mtcoding.blog.course.model;

import lombok.Data;
import shop.mtcoding.blog.domain.user.domain.User;

@Data
public class UserInfo {
    private Long id;
    private String username;
    private String email;
    private String role;

    public UserInfo(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.role = user.getRole().toKorean();
    }
}
