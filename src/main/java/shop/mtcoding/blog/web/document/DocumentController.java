package shop.mtcoding.blog.web.document;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import shop.mtcoding.blog.domain.document.DocumentService;
import shop.mtcoding.blog.domain.user.User;

@RequiredArgsConstructor
@Controller
public class DocumentController {

    private final DocumentService documentService;
    private final HttpSession session;

    @GetMapping("/api/document-menu/course")
    public String course(Model model, @PageableDefault(size = 10, direction = Sort.Direction.DESC, sort = "id") Pageable pageable) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        var modelData = documentService.과정목록(sessionUser, pageable);
        var respDTOs = modelData.coursePage().getContent().stream().map(DocumentResponse.CourseDTO::new).toList();
        model.addAttribute("models", respDTOs);
        return "document/course-list";
    }

    @GetMapping("/api/document-menu/course/{courseId}/subject")
    public String subject(@PathVariable("courseId") Long courseId, Model model) {
        var modelData = documentService.교과목목록(courseId);
        var respDTOs = modelData.subjects().stream().map(DocumentResponse.SubjectDTO::new).toList();
        model.addAttribute("models", respDTOs);
        return "document/subject-list";
    }

    @GetMapping("/api/document-menu/subject/{subjectId}")
    public String subjectDetail(@PathVariable("subjectId") Long subjectId, Model model) {
        model.addAttribute("subjectId", subjectId);
        return "document/document-list";
    }

    @GetMapping("/api/document-menu/subject/{subjectId}/no1")
    public String no1(@PathVariable("subjectId") Long subjectId, Model model) {
        var modelData = documentService.no1(subjectId);
        var respDTO = new DocumentResponse.No1DTO(modelData.subject(), modelData.questions(), modelData.teacher().getSign(), modelData.paper());
        model.addAttribute("model", respDTO);
        return "document/no1";
    }

    @GetMapping("/api/document-menu/subject/{subjectId}/no2")
    public String no2(@PathVariable("subjectId") Long subjectId, Model model) {
        var modelData = documentService.no2(subjectId);
        var respDTO = new DocumentResponse.No2DTO(modelData.subject(), modelData.questions());
        model.addAttribute("model", respDTO);
        return "document/no2";
    }

    @GetMapping("/api/document-menu/subject/{subjectId}/no3")
    public String no3(@PathVariable("subjectId") Long subjectId, Model model) {
        var modelData = documentService.no3(subjectId);
        var respDTO = new DocumentResponse.No3DTO(modelData.paper(), modelData.elements(), modelData.questions(), modelData.teacher());
        model.addAttribute("model", respDTO);
        return "document/no3";
    }

    @GetMapping("/api/document-menu/subject/{subjectId}/no4")
    public String no4(@PathVariable("subjectId") Long subjectId, Model model,
                      @RequestParam(value = "currentIndex", defaultValue = "0") Integer currentIndex) {
        var modelData = documentService.no4(subjectId, currentIndex);
        var respDTO = new DocumentResponse.No4DTO(modelData.exam(), modelData.elements(), modelData.teacher(),
                modelData.prevIndex(), modelData.nextIndex(), modelData.currentIndex());
        model.addAttribute("model", respDTO);
        return "document/no4";
    }

    @GetMapping("/api/document-menu/subject/{subjectId}/no5")
    public String no5(@PathVariable("subjectId") Long subjectId, Model model) {
        var modelData = documentService.no5(subjectId);
        var respDTO = new DocumentResponse.No5DTO(modelData.exams(), modelData.reExams(), modelData.teacher());
        model.addAttribute("model", respDTO);
        return "document/no5";
    }
}
