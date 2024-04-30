package shop.mtcoding.blog.course.subject.element;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import shop.mtcoding.blog._core.utils.ApiUtil;
import shop.mtcoding.blog.course.subject.SubjectRequest;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class SubjectElementController {
    private final HttpSession session;
    private final SubjectElementService subjectElementService;

    @GetMapping("/api/subject/{subjectId}/element")
    public String list(@PathVariable Long subjectId, Model model){
        SubjectElementResponse.ListDTO respDTO = subjectElementService.교과목요소목록(subjectId);
        model.addAttribute("model", respDTO);
        return "course/subject/element/list";
    }

    @GetMapping("/api/subject/{subjectId}/element/save-form")
    public String saveForm(@PathVariable Long subjectId, Model model){
        model.addAttribute("subjectId", subjectId);
        return "course/subject/element/save-form";
    }

    @PostMapping("/api/subject/{subjectId}/element/save")
    public ResponseEntity<?> save(@PathVariable Long subjectId, @RequestBody List<SubjectElementRequest.SaveDTO> reqDTOs){
        subjectElementService.교과목요소전체등록(subjectId, reqDTOs);
        return ResponseEntity.ok(new ApiUtil<>(null));
    }
}
