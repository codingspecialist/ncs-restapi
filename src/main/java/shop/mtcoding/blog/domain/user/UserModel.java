package shop.mtcoding.blog.domain.user;

public class UserModel {
    public record Session(User user, String accessToken, String refreshToken) {
    }
}
