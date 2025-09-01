package shop.mtcoding.blog.user.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.mtcoding.blog._core.errors.exception.api.Exception400;
import shop.mtcoding.blog.user.application.domain.User;
import shop.mtcoding.blog.user.application.repository.UserRepository;
import shop.mtcoding.blog.user.web.dto.UserRequest;
import shop.mtcoding.blog.user.web.dto.UserResponse;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmpService {

    private final UserRepository userRepository;

    @Transactional
    public UserResponse.Item 직원회원가입(UserRequest.EmpJoin request) {
        Optional<User> userOP = userRepository.findByUsername(request.username());
        if (userOP.isPresent())
            throw new Exception400("중복된 유저네임입니다.");
        User savedUser = userRepository.save(User.createEmp(request));

        return UserResponse.Item.from(savedUser);
    }
}
