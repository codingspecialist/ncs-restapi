package shop.mtcoding.blog.web.user;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import shop.mtcoding.blog.core.errors.exception.api.ApiException401;
import shop.mtcoding.blog.core.utils.ApiUtil;
import shop.mtcoding.blog.domain.user.User;
import shop.mtcoding.blog.domain.user.UserEnum;
import shop.mtcoding.blog.domain.user.UserService;


@RequiredArgsConstructor
@Controller
public class UserController {

    private final UserService userService;
    private final HttpSession session;

    @GetMapping("/join-form")
    public String joinForm() {
        return "v2/user/join-form";
    }

    @GetMapping("/login-form")
    public String loginForm() {
        return "v2/user/login-form";
    }

    @PostMapping("/join")
    public String join(UserRequest.JoinDTO reqDTO) {
        if (UserEnum.valueOf(reqDTO.getRole()) == UserEnum.STUDENT) {
            System.out.println("--------------------------------학생회원가입 시작");
            User userPS = userService.학생회원가입(reqDTO);
            System.out.println("--------------------------------학생회원가입 끝");
            session.setAttribute("isStudent", true);
            session.setAttribute("sessionUser", userPS);
            return "redirect:/api/student/exam";
        } else {
            User userPS = userService.강사회원가입(reqDTO);
            session.setAttribute("isStudent", false);
            session.setAttribute("sessionUser", userPS);
            return "redirect:/sign-form";
        }
    }

    @PutMapping("/sign")
    public ResponseEntity<?> teacherSign(@RequestBody UserRequest.TeacherSignDTO reqDTO) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (!UserEnum.TEACHER.equals(sessionUser.getRole())) throw new ApiException401("당신은 강사가 아니에요");
        User userPS = userService.강사사인저장(reqDTO, sessionUser);

        // 세션 동기화 (user - teacher(sign))
        session.setAttribute("sessionUser", userPS);
        return ResponseEntity.ok(new ApiUtil<>(null));
    }

    @GetMapping("/sign-form")
    public String teacherSignForm() {
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) return "redirect:/login-form";
        return "v2/user/sign-form";
    }

    @PostMapping("/login")
    public String login(UserRequest.LoginDTO reqDTO) {
        User userPS = userService.로그인(reqDTO);
        session.setAttribute("sessionUser", userPS);

        if (UserEnum.STUDENT.equals(userPS.getRole())) {
            session.setAttribute("isStudent", true);
            return "redirect:/api/student/exam";
        } else {
            session.setAttribute("isStudent", false);
            return "redirect:/api/emp/course";
        }
    }

    @GetMapping("/logout")
    public String logout() {
        session.invalidate();
        return "redirect:/";
    }
}
