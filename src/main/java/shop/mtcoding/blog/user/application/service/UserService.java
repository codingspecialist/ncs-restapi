package shop.mtcoding.blog.user.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.mtcoding.blog._core.errors.exception.api.Exception401;
import shop.mtcoding.blog._core.utils.JwtUtil;
import shop.mtcoding.blog.user.application.domain.User;
import shop.mtcoding.blog.user.application.repository.UserRepository;
import shop.mtcoding.blog.user.web.dto.UserRequest;
import shop.mtcoding.blog.user.web.dto.UserResponse;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserResponse.SessionItem 로그인(UserRequest.Login request) {
        User findUser = userRepository.findByUsernameAndPassword(
                request.username(), request.password())
                .orElseThrow(() -> new Exception401("인증되지 않았습니다"));

        String accessToken = JwtUtil.create(findUser);
        String refreshToken = JwtUtil.createRefresh(findUser);

        return UserResponse.SessionItem.from(findUser, accessToken, refreshToken);
    }

}