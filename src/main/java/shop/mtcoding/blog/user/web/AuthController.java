package shop.mtcoding.blog.user.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import shop.mtcoding.blog._core.utils.Resp;
import shop.mtcoding.blog.user.application.service.EmpService;
import shop.mtcoding.blog.user.application.service.StudentService;
import shop.mtcoding.blog.user.application.service.UserService;
import shop.mtcoding.blog.user.application.service.dto.UserCommand;
import shop.mtcoding.blog.user.web.dto.UserRequest;
import shop.mtcoding.blog.user.web.dto.UserResponse;

@RequiredArgsConstructor
@Controller
public class AuthController {
    private final UserService userService;
    private final StudentService studentService;
    private final EmpService empService;

    @PostMapping("/students/join")
    public ResponseEntity<?> studentJoin(@RequestBody UserRequest.StudentJoin reqDTO) {
        var command = UserCommand.StudentJoin.from(reqDTO);
        var output = studentService.학생회원가입(command);
        var respDTO = UserResponse.Item.from(output.user());
        return ResponseEntity.ok(Resp.ok(respDTO));
    }

    @PostMapping("/emps/join")
    public ResponseEntity<?> empJoin(@RequestBody UserRequest.EmpJoin reqDTO) {
        var command = UserCommand.EmpJoin.from(reqDTO);
        var output = empService.직원회원가입(command);
        var respDTO = UserResponse.Item.from(output.user());
        return ResponseEntity.ok(Resp.ok(respDTO));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserRequest.Login reqDTO) {
        var command = UserCommand.Login.from(reqDTO);
        var output = userService.로그인(command);
        var respDTO = UserResponse.SessionItem.from(output.user(), output.accessToken(), output.refreshToken());
        return ResponseEntity.ok(Resp.ok(respDTO));
    }
}