package shop.mtcoding.blog.user.student;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import shop.mtcoding.blog.course.CourseResponse;
import shop.mtcoding.blog.course.CourseService;

@RequiredArgsConstructor
@Controller
public class StudentController {
    private final HttpSession session;
    private final CourseService courseService;
    private final StudentService studentService;

    @GetMapping("/api/course/student")
    public String list(Model model, @PageableDefault(size = 10, direction = Sort.Direction.DESC, sort = "id", page = 0) Pageable pageable) {
        StudentResponse.PagingDTO respDTO = studentService.모든학생목록(pageable);
        model.addAttribute("paging", respDTO);
        return "course/student/list";
    }

    @GetMapping("/api/course/{courseId}/student/save-form")
    public String saveForm(@RequestParam(value = "courseId", required = false) Long courseId, Model model) {
        CourseResponse.SelectedDTO respDTO = courseService.선택된과정목록(courseId);
        model.addAttribute("model", respDTO);
        model.addAttribute("screen", courseId == null ? "" : "detail");

        return "course/student/save-form";
    }

    @PostMapping("/api/course/{courseId}/student/save")
    public String save(@PathVariable(value = "courseId") Long courseId, StudentRequest.SaveDTO reqDTO) {
        studentService.학생등록(courseId, reqDTO);
        return "redirect:/api/teacher/course/" + courseId + "?tabNum=1";
    }
}
