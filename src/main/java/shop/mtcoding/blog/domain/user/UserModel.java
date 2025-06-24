package shop.mtcoding.blog.domain.user;

public class UserModel {
    public record Session(SessionUser sessionUser, String accessToken, String refreshToken) {
    }
}
