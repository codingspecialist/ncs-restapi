package shop.mtcoding.blog.webv2.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import shop.mtcoding.blog._core.utils.Resp;
import shop.mtcoding.blog.domainv2222222.user.UserService;

@RequiredArgsConstructor
@Controller
public class UserController {
    private final UserService userService;

    @PostMapping("/teachers/join")
    public ResponseEntity<?> teacherJoin(UserRequest.TeacherJoin reqDTO) {
        var modelData = userService.강사_회원가입(reqDTO);
        var respDTO = new UserResponse.Item(modelData.user());
        return ResponseEntity.ok(Resp.ok(respDTO));
    }

    @PostMapping("/students/join")
    public ResponseEntity<?> studentJoin(UserRequest.StudentJoin reqDTO) {
        var modelData = userService.학생_회원가입(reqDTO);
        var respDTO = new UserResponse.Item(modelData.user());
        return ResponseEntity.ok(Resp.ok(respDTO));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(UserRequest.Login reqDTO) {
        var modelData = userService.로그인(reqDTO);
        var respDTO = new UserResponse.Session(modelData.user(), modelData.accessToken(), modelData.refreshToken());
        return ResponseEntity.ok(Resp.ok(respDTO));
    }
}
