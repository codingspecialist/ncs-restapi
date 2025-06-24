package shop.mtcoding.blog.domain.user;

public class UserModel {
    public record Item(User user, String accessToken, String refreshToken) {
    }
}
