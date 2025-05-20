package shop.mtcoding.blog.user;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import shop.mtcoding.blog._core.errors.exception.api.ApiException401;
import shop.mtcoding.blog._core.utils.ApiUtil;


@RequiredArgsConstructor
@Controller
public class UserController {

    private final UserService userService;
    private final HttpSession session;

    @GetMapping("/join-form")
    public String joinForm() {
        return "user/join-form";
    }

    @GetMapping("/login-form")
    public String loginForm() {
        return "user/login-form";
    }

    @PostMapping("/join")
    public String join(UserRequest.JoinDTO reqDTO) {
        if (UserEnum.valueOf(reqDTO.getRole()) == UserEnum.STUDENT) {
            User sessionUser = userService.학생회원가입(reqDTO);
            session.setAttribute("isStudent", true);
            session.setAttribute("sessionUser", sessionUser);
            return "redirect:/api/student/exam";
        } else {
            User sessionUser = userService.강사회원가입(reqDTO);
            session.setAttribute("isStudent", false);
            session.setAttribute("sessionUser", sessionUser);
            return "redirect:/teacher/sign-form";
        }
    }

    @PutMapping("/teacher/sign")
    public ResponseEntity<?> sign(@RequestBody UserRequest.TeacherSignDTO reqDTO) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser.getRole().equals("student")) throw new ApiException401("당신은 선생님이 아니에요");
        userService.사인저장(reqDTO, sessionUser);
        session.invalidate();
        return ResponseEntity.ok(new ApiUtil<>(null));
    }

    @GetMapping("/teacher/sign-form")
    public String teacherSignForm() {
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) return "redirect:/login-form";
        return "user/teacher-sign-form";
    }

    @PostMapping("/login")
    public String login(UserRequest.LoginDTO reqDTO) {
        User sessionUser = userService.로그인(reqDTO);
        session.setAttribute("sessionUser", sessionUser);

        System.out.println("================ " + sessionUser.getRole());
        if (UserEnum.STUDENT.equals(sessionUser.getRole())) {
            session.setAttribute("isStudent", true);
            return "redirect:/api/student/exam";
        } else {
            session.setAttribute("isStudent", false);
            return "redirect:/";
        }
    }

    @GetMapping("/logout")
    public String logout() {
        session.invalidate();
        return "redirect:/";
    }
}
