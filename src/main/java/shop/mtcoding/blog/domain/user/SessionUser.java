package shop.mtcoding.blog.domain.user;

import lombok.Data;

@Data
public class SessionUser {
    Long userId;
    UserType role;
    String accessToken;
    String refreshToken;

    public SessionUser(Long userId, UserType role, String accessToken, String refreshToken) {
        this.userId = userId;
        this.role = role;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
