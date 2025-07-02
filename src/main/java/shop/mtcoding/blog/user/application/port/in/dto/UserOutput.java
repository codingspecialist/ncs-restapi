package shop.mtcoding.blog.user.application.port.in.dto;

import shop.mtcoding.blog.user.domain.User;

// 레이지 로딩이 끝난 모델 데이터를 가져오자
public class UserOutput {
    public record Login(User user, String accessToken, String refreshToken) {
    }
}
