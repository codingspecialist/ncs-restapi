package shop.mtcoding.blog.user.adapter.in.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import shop.mtcoding.blog._core.utils.Resp;
import shop.mtcoding.blog.user.adapter.in.web.dto.UserRequest;
import shop.mtcoding.blog.user.adapter.in.web.dto.UserResponse;
import shop.mtcoding.blog.user.application.port.in.EmpUseCase;
import shop.mtcoding.blog.user.application.port.in.StudentUseCase;
import shop.mtcoding.blog.user.application.port.in.TeacherUseCase;
import shop.mtcoding.blog.user.application.port.in.UserUseCase;
import shop.mtcoding.blog.user.application.port.in.dto.UserCommand;

@RequiredArgsConstructor
@Controller
public class UserController {

    private final StudentUseCase studentUseCase;
    private final TeacherUseCase teacherUseCase;
    private final EmpUseCase empUseCase;
    private final UserUseCase userUseCase;

    @PostMapping("/teachers/join")
    public ResponseEntity<?> teacherJoin(@RequestBody UserRequest.TeacherJoin reqDTO) {
        var command = UserCommand.TeacherJoin.from(reqDTO);
        var output = teacherUseCase.강사회원가입(command);
        var respDTO = UserResponse.Item.from(output);
        return ResponseEntity.ok(Resp.ok(respDTO));
    }

    @PostMapping("/students/join")
    public ResponseEntity<?> studentJoin(@RequestBody UserRequest.StudentJoin reqDTO) {
        var command = UserCommand.StudentJoin.from(reqDTO);
        var output = studentUseCase.학생회원가입(command);
        var respDTO = UserResponse.Item.from(output);
        return ResponseEntity.ok(Resp.ok(respDTO));
    }

    @PostMapping("/emps/join")
    public ResponseEntity<?> empJoin(@RequestBody UserRequest.EmpJoin reqDTO) {
        var command = UserCommand.EmpJoin.from(reqDTO);
        var output = empUseCase.직원회원가입(command);
        var respDTO = UserResponse.Item.from(output);
        return ResponseEntity.ok(Resp.ok(respDTO));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserRequest.Login reqDTO) {
        var command = UserCommand.Login.from(reqDTO);
        var output = userUseCase.로그인(command);
        var respDTO = UserResponse.SessionItem.from(output.user(), output.accessToken(), output.refreshToken());
        return ResponseEntity.ok(Resp.ok(respDTO));
    }
}