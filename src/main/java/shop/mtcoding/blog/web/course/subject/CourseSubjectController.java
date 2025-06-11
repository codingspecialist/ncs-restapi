package shop.mtcoding.blog.web.course.subject;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import shop.mtcoding.blog.domain.course.subject.SubjectService;

@RequiredArgsConstructor
@Controller
public class CourseSubjectController {

    private final SubjectService subjectService;

    @GetMapping("/api/course-menu/course/{courseId}/subject/save-form")
    public String saveForm(@PathVariable("courseId") Long courseId, @RequestParam("courseTitle") String courseTitle, @RequestParam("courseRound") String courseRound, Model model) {
        model.addAttribute("courseId", courseId);
        model.addAttribute("courseTitle", courseTitle);
        model.addAttribute("courseRound", courseRound);
        return "course/subject/save-form";
    }

    @PostMapping("/api/course-menu/course/{courseId}/subject/save")
    public String save(@PathVariable(value = "courseId") Long courseId, CourseSubjectRequest.SaveDTO reqDTO) {
        subjectService.교과목등록(courseId, reqDTO);
        return "redirect:/api/course-menu/course/" + courseId + "?tabNum=0";
    }

}
