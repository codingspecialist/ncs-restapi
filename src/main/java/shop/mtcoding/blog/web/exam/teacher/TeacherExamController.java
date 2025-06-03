package shop.mtcoding.blog.web.exam.teacher;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import shop.mtcoding.blog.domain.course.CourseModel;
import shop.mtcoding.blog.domain.course.CourseService;
import shop.mtcoding.blog.domain.course.subject.SubjectService;
import shop.mtcoding.blog.domain.user.User;

@RequiredArgsConstructor
@Controller
public class TeacherExamController {

    private final CourseService courseService;
    private final SubjectService subjectService;
    private final HttpSession session;

    @GetMapping("/api/exam-menu/course")
    public String list(Model model, @PageableDefault(size = 10, direction = Sort.Direction.DESC, sort = "id", page = 0) Pageable pageable) {
        User sessionUser = (User) session.getAttribute("sessionUser");

        CourseModel.Items items = courseService.과정목록(sessionUser.getTeacher().getId(), pageable);
        TeacherExamResponse.CourseListDTO respDTO = new TeacherExamResponse.CourseListDTO(items.coursePG());
        model.addAttribute("model", respDTO);

        return "v2/exam/teacher/course-list";
    }


}
