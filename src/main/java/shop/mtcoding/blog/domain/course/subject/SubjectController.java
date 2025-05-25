package shop.mtcoding.blog.domain.course.subject;

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

@RequiredArgsConstructor
@Controller
public class SubjectController {

    private final SubjectService subjectService;

    @GetMapping("/api/course/subject")
    public String list(Model model, @PageableDefault(size = 10, direction = Sort.Direction.DESC, sort = "id", page = 0) Pageable pageable) {
        SubjectResponse.PagingDTO respDTO = subjectService.모든교과목목록(pageable);
        model.addAttribute("paging", respDTO);
        return "course/subject/list";
    }

    @GetMapping("/api/course/{courseId}/subject/save-form")
    public String saveForm(@PathVariable("courseId") Long courseId, @RequestParam("courseTitle") String courseTitle, @RequestParam("courseRound") String courseRound, Model model) {
        model.addAttribute("courseId", courseId);
        model.addAttribute("courseTitle", courseTitle);
        model.addAttribute("courseRound", courseRound);
        return "course/subject/save-form";
    }

    @PostMapping("/api/course/{courseId}/subject/save")
    public String save(@PathVariable(value = "courseId") Long courseId, SubjectRequest.SaveDTO reqDTO) {
        subjectService.교과목등록(courseId, reqDTO);
        return "redirect:/api/course/" + courseId + "?tabNum=0";
    }

}
