package shop.mtcoding.blog.webv2.course.student;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import shop.mtcoding.blog.domainv2222222.user.student.StudentService;

@RequiredArgsConstructor
@Controller
public class CourseStudentController {

    private final HttpSession session;
    private final StudentService studentService;

    @GetMapping("/api/course-menu/course/{courseId}/student/save-form")
    public String studentSaveForm(@PathVariable("courseId") Long courseId, @RequestParam("courseTitle") String courseTitle, @RequestParam("courseRound") String courseRound, Model model) {
        model.addAttribute("courseId", courseId);
        model.addAttribute("courseTitle", courseTitle);
        model.addAttribute("courseRound", courseRound);
        return "course/student/save-form";
    }

    @PostMapping("/api/course-menu/course/{courseId}/student/save")
    public String studentSave(@PathVariable(value = "courseId") Long courseId, CourseStudentRequest.SaveDTO reqDTO) {
        studentService.학생등록(courseId, reqDTO);
        return "redirect:/api/course-menu/course/" + courseId + "?tabNum=1";
    }
}
