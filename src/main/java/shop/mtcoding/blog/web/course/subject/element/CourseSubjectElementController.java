package shop.mtcoding.blog.web.course.subject.element;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import shop.mtcoding.blog.core.utils.ApiUtil;
import shop.mtcoding.blog.domain.course.subject.element.SubjectElementService;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class CourseSubjectElementController {
    private final HttpSession session;
    private final SubjectElementService subjectElementService;

    @GetMapping("/api/course-menu/subject/{subjectId}/element")
    public String list(@PathVariable(value = "subjectId") Long subjectId, Model model) {
        CourseSubjectElementResponse.ListDTO respDTO = subjectElementService.교과목요소목록(subjectId);
        model.addAttribute("model", respDTO);
        return "v2/course/subject/element/list";
    }

    @GetMapping("/api/course-menu/subject/{subjectId}/element/save-form")
    public String saveForm(@PathVariable(value = "subjectId") Long subjectId, Model model) {
        model.addAttribute("subjectId", subjectId);
        return "v2/course/subject/element/save-form";
    }

    @PostMapping("/api/course-menu/subject/{subjectId}/element/save")
    public ResponseEntity<?> save(@PathVariable(value = "subjectId") Long subjectId, @RequestBody List<CourseSubjectElementRequest.SaveDTO> reqDTOs) {
        subjectElementService.교과목요소전체등록(subjectId, reqDTOs);
        return ResponseEntity.ok(new ApiUtil<>(null));
    }
}
