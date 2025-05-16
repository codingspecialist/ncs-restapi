package shop.mtcoding.blog.paper;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import shop.mtcoding.blog._core.utils.ApiUtil;
import shop.mtcoding.blog.course.subject.SubjectResponse;
import shop.mtcoding.blog.course.subject.SubjectService;
import shop.mtcoding.blog.paper.question.QuestionDBResponse;

import java.util.List;

/**
 * 1. 시험지
 */

@RequiredArgsConstructor
@Controller
public class PaperController {
    private final HttpSession session;
    private final PaperService paperService;
    private final SubjectService subjectService;

    @PostMapping("/api/teacher/paper/{paperId}/question/save")
    public ResponseEntity<?> questionSave(@RequestBody PaperRequest.QuestionSaveDTO reqDTO) {
        paperService.문제등록(reqDTO);
        return ResponseEntity.ok(new ApiUtil<>(null));
    }

    @GetMapping("/api/teacher/paper/save-form")
    public String getList(Model model) {
        List<SubjectResponse.DTO> respDTO = subjectService.모든교과목목록();
        model.addAttribute("models", respDTO);
        return "paper/save-form";
    }

    @PostMapping("/api/teacher/paper/save")
    public String save(PaperRequest.SaveDTO reqDTO) {
        paperService.시험지등록(reqDTO);
        return "redirect:/api/teacher/paper";
    }

    @GetMapping("/api/teacher/paper")
    public String list(Model model, @PageableDefault(size = 10, direction = Sort.Direction.DESC, sort = "id", page = 0) Pageable pageable) {
        PaperResponse.PagingDTO respDTO = paperService.시험지목록(pageable);
        model.addAttribute("paging", respDTO);
        return "paper/list";
    }

    @GetMapping("/api/teacher/paper/{paperId}")
    public String detail(@PathVariable(value = "paperId") Long paperId, Model model) {
        PaperResponse.QuestionListDTO respDTO = paperService.문제목록(paperId);
        model.addAttribute("model", respDTO);
        return "paper/detail";
    }

    @GetMapping("/api/teacher/paper/{paperId}/question")
    public String questionSaveForm(@PathVariable(name = "paperId") Long paperId, Model model) {
        QuestionDBResponse.ExpectedNextDTO respDTO = paperService.다음예상문제(paperId);
        model.addAttribute("expectNo", respDTO.getExpectNo());
        model.addAttribute("expectPoint", respDTO.getExpectPoint());
        model.addAttribute("paperId", paperId);
        model.addAttribute("elements", respDTO.getElements());
        return "paper/question/save-form";
    }
}
