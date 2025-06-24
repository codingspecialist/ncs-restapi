package shop.mtcoding.blog.web.user;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import shop.mtcoding.blog.core.errors.exception.api.Exception401;
import shop.mtcoding.blog.core.utils.ApiUtil;
import shop.mtcoding.blog.domain.user.User;
import shop.mtcoding.blog.domain.user.UserModel;
import shop.mtcoding.blog.domain.user.UserService;
import shop.mtcoding.blog.domain.user.UserType;


@RequiredArgsConstructor
@Controller
public class UserController {

    private final UserService userService;
    private final HttpSession session;


    @PostMapping("/join")
    public String join(UserRequest.JoinDTO reqDTO) {
        if (UserType.valueOf(reqDTO.getRole()) == UserType.STUDENT) {
            User userPS = userService.학생회원가입(reqDTO);
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
        if (!UserType.TEACHER.equals(sessionUser.getRole())) throw new Exception401("당신은 강사가 아니에요");
        User userPS = userService.강사사인저장(reqDTO, sessionUser);

        // 세션 동기화 (user - teacher(sign))
        session.setAttribute("sessionUser", userPS);
        return ResponseEntity.ok(new ApiUtil<>(null));
    }

    @GetMapping("/sign-form")
    public String teacherSignForm() {
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) return "redirect:/login-form";
        return "user/sign-form";
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(UserRequest.LoginDTO reqDTO) {
        UserModel.Session modelData = userService.로그인(reqDTO);
        session.setAttribute("sessionUser", modelData.user());

        return ResponseEntity.ok();
    }

    @GetMapping("/logout")
    public String logout() {
        session.invalidate();
        return "redirect:/";
    }
}
