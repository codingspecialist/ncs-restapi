package shop.mtcoding.blog.domain.user.application.dto;

import shop.mtcoding.blog.domain.user.model.User;

public class UserResult {
    public record Login(User user, String accessToken, String refreshToken) {
    }

    public record StudentJoin(User user) {
    }

    public record TeacherJoin(User user) {
    }
}
