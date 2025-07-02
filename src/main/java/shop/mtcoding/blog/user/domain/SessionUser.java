package shop.mtcoding.blog.user.domain;

import lombok.Builder;
import lombok.Data;
import shop.mtcoding.blog.user.domain.enums.UserRole;

@Data
public class SessionUser {
    Long userId;
    UserRole role;
    String accessToken;
    String refreshToken;

    @Builder
    public SessionUser(Long userId, UserRole role, String accessToken, String refreshToken) {
        this.userId = userId;
        this.role = role;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
