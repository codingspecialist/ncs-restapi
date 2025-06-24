package shop.mtcoding.blog.web.user;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import shop.mtcoding.blog.core.utils.Resp;
import shop.mtcoding.blog.domain.user.SessionUser;
import shop.mtcoding.blog.domain.user.User;
import shop.mtcoding.blog.domain.user.UserService;


@RequiredArgsConstructor
@Controller
public class UserController {

    private final UserService userService;
    private final HttpSession session;

    @PostMapping("/teachers/join")
    public ResponseEntity<?> teacherJoin(UserRequest.TeacherJoin reqDTO) {
        User userPS = userService.강사_회원가입(reqDTO);
        return ResponseEntity.ok(Resp.ok(null));
    }

    @PostMapping("/students/join")
    public ResponseEntity<?> studentJoin(UserRequest.StudentJoin reqDTO) {
        User userPS = userService.학생_회원가입(reqDTO);
        return ResponseEntity.ok(Resp.ok(null));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(UserRequest.LoginDTO reqDTO) {
        SessionUser sessionUser = userService.로그인(reqDTO);
        return ResponseEntity.ok(sessionUser);
    }

    @GetMapping("/logout")
    public String logout() {
        session.invalidate();
        return "redirect:/";
    }
}
