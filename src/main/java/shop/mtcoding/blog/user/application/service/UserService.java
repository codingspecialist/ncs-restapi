package shop.mtcoding.blog.user.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.mtcoding.blog._core.errors.exception.api.Exception401;
import shop.mtcoding.blog._core.utils.JwtUtil;
import shop.mtcoding.blog.user.application.domain.User;
import shop.mtcoding.blog.user.application.repository.UserRepository;
import shop.mtcoding.blog.user.application.service.dto.UserCommand;
import shop.mtcoding.blog.user.application.service.dto.UserOutput;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserOutput.Session 로그인(UserCommand.Login command) {
        User findUser = userRepository.findByUsernameAndPassword(
                        command.username(), command.password())
                .orElseThrow(() -> new Exception401("인증되지 않았습니다"));

        String accessToken = JwtUtil.create(findUser);
        String refreshToken = JwtUtil.createRefresh(findUser);

        return new UserOutput.Session(findUser, accessToken, refreshToken);
    }


}