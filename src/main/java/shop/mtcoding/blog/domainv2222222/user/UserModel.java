package shop.mtcoding.blog.domainv2222222.user;

public class UserModel {
    public record Item(User user, String accessToken, String refreshToken) {
    }
}
